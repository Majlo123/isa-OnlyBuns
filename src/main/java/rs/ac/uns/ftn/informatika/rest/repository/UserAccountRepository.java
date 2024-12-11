package rs.ac.uns.ftn.informatika.rest.repository;

import jakarta.persistence.LockModeType;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserAccountRepository {

    UserAccount findById(Long id);
    UserAccount create(UserAccount userAccount);
    UserAccount update(UserAccount userAccount);
    UserAccount delete(Long id);
    Collection<UserAccount> findAll();


}
