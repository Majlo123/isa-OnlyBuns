package rs.ac.uns.ftn.informatika.rest.repository;

import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserAccountRepository implements UserAccountRepository {
    private static AtomicLong counter = new AtomicLong();
    private final ConcurrentMap<Long, UserAccount> userAccounts = new ConcurrentHashMap<>();

    @Override
    public Collection<UserAccount> findAll() { return userAccounts.values(); }

    @Override
    public UserAccount findById(Long id) { return userAccounts.get(id); }

    @Override
    public UserAccount create(UserAccount userAccount) {
        Long id = counter.incrementAndGet();
        userAccount.setId(id);
        userAccounts.put(id, userAccount);
        return userAccount;
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
    }


}
