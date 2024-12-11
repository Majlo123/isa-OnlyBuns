package rs.ac.uns.ftn.informatika.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.repository.FollowRepository;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryUserAccountRepository;
import rs.ac.uns.ftn.informatika.rest.service.UserAccountService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(statements = {
        "TRUNCATE TABLE public.user_accounts RESTART IDENTITY CASCADE;",
        "INSERT INTO public.user_accounts (acc_id, address, email, first_name, followers_count, is_enabled, last_name, password, post_count, role, user_name, verification_code, last_time_used) VALUES (1, '{\"street\":\"Filipa Visnjica\",\"city\":\"Novi Sad\",\"country\":\"Serbia\",\"number\":\"1\"}', 'target@example.com', 'Target', 0, true, 'User', 'password', 0, 'USER', 'targetuser', null, now());",
        "INSERT INTO public.user_accounts (acc_id, address, email, first_name, followers_count, is_enabled, last_name, password, post_count, role, user_name, verification_code, last_time_used) VALUES (2, '{\"street\":\"Filipa Visnjica\",\"city\":\"Novi Sad\",\"country\":\"Serbia\",\"number\":\"2\"}', 'follower1@example.com', 'Follower', 0, true, 'One', 'password', 0, 'USER', 'followerone', null, now());",
        "INSERT INTO public.user_accounts (acc_id, address, email, first_name, followers_count, is_enabled, last_name, password, post_count, role, user_name, verification_code, last_time_used) VALUES (3, '{\"street\":\"Filipa Visnjica\",\"city\":\"Novi Sad\",\"country\":\"Serbia\",\"number\":\"3\"}', 'follower2@example.com', 'Follower', 0, true, 'Two', 'password', 0, 'USER', 'followertwo', null, now());"
})

public class UserAccountServiceTest {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private InMemoryUserAccountRepository userAccountRepository;

    @Autowired
    private FollowRepository followRepository;

    @AfterEach
    public void tearDown() {
        followRepository.deleteAll();
        userAccountRepository.deleteAll();
    }

    @Test
    public void testConcurrentFollow() throws InterruptedException {
        // Pošto smo ubacili podatke preko @Sql, ne moramo da koristimo save().
        // Sada samo učitavamo targetUser i followere iz baze.

        UserAccount targetUser = userAccountRepository.findByEmail("target@example.com");
        UserAccount follower1 = userAccountRepository.findByEmail("follower1@example.com");
        UserAccount follower2 = userAccountRepository.findByEmail("follower2@example.com");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable followTask1 = () -> userAccountService.followUser(follower1.getId(), targetUser.getId());
        Runnable followTask2 = () -> userAccountService.followUser(follower2.getId(), targetUser.getId());

        executor.submit(followTask1);
        executor.submit(followTask2);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        UserAccount updatedTargetUser = userAccountRepository.findById(targetUser.getId())
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));

        assertEquals(2, updatedTargetUser.getFollowersCount());
    }
}
