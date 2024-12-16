package rs.ac.uns.ftn.informatika.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.informatika.rest.domain.Address;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryUserAccountRepository;
import rs.ac.uns.ftn.informatika.rest.repository.UserAccountRepository;
import rs.ac.uns.ftn.informatika.rest.service.UserAccountService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterAccountTest {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private InMemoryUserAccountRepository userAccountRepository;

    @MockBean
    private HttpServletRequest mockRequest; // Mock the HttpServletRequest

    @Before
    public void setup() {
        // Mock HttpServletRequest methods
        when(mockRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/test"));
        when(mockRequest.getRequestURI()).thenReturn("/test");
        when(mockRequest.getHeader("Host")).thenReturn("localhost:8080");
        when(mockRequest.getServletPath()).thenReturn("/testPath");
    }
    @Test(expected = PessimisticLockingFailureException.class)
    public void testCreateUserWithConcurrentAccess() throws Throwable {
        // Arrange: Prepare the account DTO
        UserAccountDTO accountDTO = new UserAccountDTO();
        accountDTO.setEmail("test12@example.com");
        accountDTO.setPassword("password");
        accountDTO.setFirstName("Test");
        accountDTO.setLastName("Test");
        accountDTO.setAddress(new Address());
        accountDTO.setFollowersCount(0);
        accountDTO.setPostCount(0);


        // Mock the findByEmail method to simulate a pessimistic write lock on the email
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 1");
                try {
                    userAccountService.create(accountDTO,mockRequest); // izvrsavanje transakcione metode traje oko 200 milisekundi
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        Future<?> future2 = executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 2");
                try { Thread.sleep(50); } catch (InterruptedException e) { }// otprilike 150 milisekundi posle prvog threada krece da se izvrsava drugi
                /*
                 * Drugi thread pokusava da izvrsi transakcionu metodu findOneById dok se prvo izvrsavanje iz prvog threada jos nije zavrsilo.
                 * Metoda je oznacena sa NO_WAIT, sto znaci da drugi thread nece cekati da prvi thread zavrsi sa izvrsavanjem metode vec ce odmah dobiti PessimisticLockingFailureException uz poruke u logu:
                 * [pool-1-thread-2] o.h.engine.jdbc.spi.SqlExceptionHelper : SQL Error: 0, SQLState: 55P03
                 * [pool-1-thread-2] o.h.engine.jdbc.spi.SqlExceptionHelper : ERROR: could not obtain lock on row in relation "product"
                 * Prema Postgres dokumentaciji https://www.postgresql.org/docs/9.3/errcodes-appendix.html, kod 55P03 oznacava lock_not_available
                 */
                try {
                    userAccountService.create(accountDTO,mockRequest);
                } catch (Exception e) {
                    if (e instanceof PessimisticLockingFailureException) {
                        System.out.println("Caught PessimisticLockingFailureException");
                        throw new PessimisticLockingFailureException(e.getMessage());
                    } else {
                        System.out.println("Caught unknown exception: " + e.getClass().getName());
                    }
                }
            }
        });
        try {
            future2.get(); // podize ExecutionException za bilo koji izuzetak iz drugog child threada
        } catch (ExecutionException e) {
            System.out.println("Exception from thread " + e.getCause().getClass()); // u pitanju je bas PessimisticLockingFailureException
            throw e.getCause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
