package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.informatika.rest.domain.AuthRequest;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;

import java.util.Collection;
import java.util.List;

public interface UserAccountService {

    UserAccount create(UserAccountDTO userAccountDto) throws Exception;
    Page<UserAccount> findAll(Pageable pageable);
    UserAccount findById(Long id);
    UserAccount update(UserAccountDTO userAccountDto, Long id) throws Exception;
    UserAccount delete(Long id);
    String verify(AuthRequest authRequest);
    List<UserAccount> searchByFirstName(String firstName);
    List<UserAccount> searchByLastName(String lastName);
    List<UserAccount> searchByEmail(String email);
    List<UserAccount> searchByPostCount(int min, int max);
    List<UserAccount> sortByFollowingCount();
    List<UserAccount> sortByEmail();
}
