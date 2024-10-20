package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryUserAccountRepository;
import rs.ac.uns.ftn.informatika.rest.repository.UserAccountRepository;

import java.util.Collection;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private InMemoryUserAccountRepository userAccountRepository;

    @Override
    public Collection<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }

    @Override
    public UserAccount findById(Long id) {
        return userAccountRepository.findById(id);
    }

    @Override
    public UserAccount create(UserAccountDTO accountDTO) throws Exception {

        UserAccount savedAcc = userAccountRepository.create(new UserAccount(accountDTO));
        return savedAcc;
    }

    @Override
    public UserAccount delete(Long id) {
        return userAccountRepository.delete(id);
    }

    @Override
    public  UserAccount update(UserAccountDTO userAccountDto, Long id) throws Exception {
        UserAccount accToUpdate = userAccountRepository.findById(id);
        if(accToUpdate == null) {
            throw new Exception("Trazeni nalog nije pronadjen.");
        }
        return userAccountRepository.update(new UserAccount(userAccountDto));
    }


}
