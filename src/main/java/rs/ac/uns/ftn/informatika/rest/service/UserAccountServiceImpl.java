package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.AuthRequest;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryUserAccountRepository;
import rs.ac.uns.ftn.informatika.rest.repository.UserAccountRepository;

import java.util.Collection;
import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private final InMemoryUserAccountRepository userAccountRepository;

    @Autowired
    private AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private JwtService jwtService;

    @Autowired
    public UserAccountServiceImpl(InMemoryUserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Page<UserAccount> findAll(Pageable pageable) {
        return userAccountRepository.findAll(pageable);
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
    public UserAccount update(UserAccountDTO userAccountDto, Long id) throws Exception {
        return userAccountRepository.findById(id)
                .map(existingUserAccount -> {
                    existingUserAccount.setFirstName(userAccountDto.getFirstName());
                    existingUserAccount.setLastName(userAccountDto.getLastName());
                    existingUserAccount.setEmail(userAccountDto.getEmail());
                    existingUserAccount.setPassword(userAccountDto.getPassword());
                    existingUserAccount.setFollowersCount(userAccountDto.getFollowersCount());
                    existingUserAccount.setAddress(userAccountDto.getAddress());
                    return userAccountRepository.save(existingUserAccount);
                })
                .orElseThrow(() -> new Exception("UserAccount not found with id: " + id));
    }

    @Override
    public String verify(AuthRequest credentials) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );
        return authentication.isAuthenticated() ? jwtService.generateToken(credentials.getUsername()) : "Failure";
    }
    public List<UserAccount> searchByFirstName(String firstName) {
        return userAccountRepository.findByFirstNameContaining(firstName);
    }

    public List<UserAccount> searchByLastName(String lastName) {
        return userAccountRepository.findByLastNameContaining(lastName);
    }

    public List<UserAccount> searchByEmail(String email) {
        return userAccountRepository.findByEmailContaining(email);
    }

    public List<UserAccount> searchByPostCount(int minPosts, int maxPosts) {
        return userAccountRepository.findByPostCountBetween(minPosts, maxPosts);
    }

    public List<UserAccount> sortByFollowingCount() {
        return userAccountRepository.findAllSortedByFollowingCount();
    }

    public List<UserAccount> sortByEmail() {
        return userAccountRepository.findAllSortedByEmail();
    }
}
