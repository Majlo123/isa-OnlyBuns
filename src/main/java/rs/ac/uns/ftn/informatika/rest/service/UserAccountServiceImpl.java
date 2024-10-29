package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.AuthRequest;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryUserAccountRepository;
import rs.ac.uns.ftn.informatika.rest.repository.UserAccountRepository;

import java.util.Collection;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private final InMemoryUserAccountRepository userAccountRepository;

    @Autowired
    AuthenticationManager authManager;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private JwtService jwtService;

    @Autowired
    public UserAccountServiceImpl(InMemoryUserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Collection<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }

    @Override
    public UserAccount findById(Long id) {
        return userAccountRepository.findById(id).orElse(null);
    }

    @Override
    public UserAccount create(UserAccountDTO accountDTO) throws Exception {
        accountDTO.setPassword(encoder.encode(accountDTO.getPassword()));
        UserAccount savedAcc = userAccountRepository.save(new UserAccount(accountDTO));
        return savedAcc;
    }

    @Override
    public UserAccount delete(Long id) {
        UserAccount deletedAcc = userAccountRepository.findById(id).orElse(null);
        userAccountRepository.deleteById(id);
        return deletedAcc;
    }

    @Override
    public  UserAccount update(UserAccountDTO userAccountDto, Long id) throws Exception {
        /*UserAccount accToUpdate = userAccountRepository.findById(id);
        if(accToUpdate == null) {
            throw new Exception("Trazeni nalog nije pronadjen.");
        }
        return userAccountRepository.update(new UserAccount(userAccountDto));*/
        return userAccountRepository.findById(id)
                .map(existingUserAccount -> {
                    // Update only the fields that need to be changed
                    existingUserAccount.setFirstName(userAccountDto.getFirstName());
                    existingUserAccount.setLastName(userAccountDto.getLastName());
                    existingUserAccount.setEmail(userAccountDto.getEmail());
                    existingUserAccount.setPassword(userAccountDto.getPassword());
                    existingUserAccount.setFollowersCount(userAccountDto.getFollowersCount());
                    existingUserAccount.setAddress(userAccountDto.getAddress());
                    // Add any other fields that need to be updated

                    // Save the updated entity to the database
                    return userAccountRepository.save(existingUserAccount);
                })
                .orElseThrow(() -> new Exception("UserAccount not found with id: " + id));
    }
    @Override
    public String verify(AuthRequest credentials){
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(credentials.getUsername());
        }else {
            return "Failure";
        }
    }
    }



