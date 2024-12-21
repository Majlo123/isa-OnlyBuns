package rs.ac.uns.ftn.informatika.rest.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface InMemoryUserAccountRepository extends JpaRepository<UserAccount, Long> {


   @Lock(LockModeType.PESSIMISTIC_WRITE)
   @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="-1")})
   UserAccount findByEmail(@Param("email")String email);
   List<UserAccount> findByFirstNameContaining(String firstName);

   List<UserAccount> findByLastNameContaining(String lastName);

   List<UserAccount> findByEmailContaining(String email);

   List<UserAccount> findByPostCountBetween(int minPosts, int maxPosts);
   @Lock(LockModeType.PESSIMISTIC_WRITE)
   @Query("SELECT u FROM UserAccount u WHERE u.id = :id")
   Optional<UserAccount> findByIdWithLock(@Param("id") Long id);
   List<UserAccount> findAllByIsEnabledFalseAndIsDeletedFalse();
   @Query("SELECT u FROM UserAccount u ORDER BY u.followersCount ASC")
   List<UserAccount> findAllSortedByFollowingCount();

   @Query("SELECT u FROM UserAccount u ORDER BY u.email ASC")
   List<UserAccount> findAllSortedByEmail();
   @Transactional(readOnly = false)
   UserAccount findByVerificationCode(String verificationCode);

   @Transactional
   @Modifying
   @Query(value = "LOCK TABLE user_account IN EXCLUSIVE MODE", nativeQuery = true)
   void lockTable();
    /*private static AtomicLong counter = new AtomicLong();
    private final ConcurrentMap<Long, UserAccount> userAccounts = new ConcurrentHashMap<>();

    @Override
    public Collection<UserAccount> findAll() { return userAccounts.values(); }

    @Override
    public UserAccount findById(Long id) { return userAccounts.get(id); }

    @Override
    public UserAccount create(UserAccount userAccount) {

        //userAccounts.put(userAccount.getId(), userAccount);
        return userAcc;
    }

    @Override
    public UserAccount update(UserAccount userAccount) {
        Long id = userAccount.getId();
        userAccounts.put(id, userAccount);
        return userAccount;
    }

    @Override
    public UserAccount delete(Long id) {
        UserAccount userAccount = userAccounts.remove(id);
        return userAccount;
    }*/


}
