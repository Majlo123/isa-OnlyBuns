package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.domain.UserInfo;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryUserAccountRepository;

@Service
public class ChangeUserInfoServiceImpl implements ChangeUserInfoService {

    @Autowired
    private final InMemoryUserAccountRepository userAccountRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public ChangeUserInfoServiceImpl(InMemoryUserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserAccount changeFirstAndLastName(UserInfo newUserInfo) {
        UserAccount userInfo = userAccountRepository.findByEmail(newUserInfo.email);
        if(userInfo != null) {
            userInfo.setFirstName(newUserInfo.firstName);
            userInfo.setLastName(newUserInfo.lastName);
            userAccountRepository.save(userInfo);
            return userInfo;
        }else{
            return null;
        }

    }

    @Override
    public UserAccount changeAddress(UserInfo newUserInfo) {
        UserAccount userInfo = userAccountRepository.findByEmail(newUserInfo.email);
        if(userInfo != null) {
            String jsonAddress = userInfo.convertAddressToJson(newUserInfo.address);
            userInfo.setAddress(jsonAddress);

            userAccountRepository.save(userInfo);
            return userInfo;
        }else{
            return null;
        }
    }

    @Override
    public String changePassword(UserAccountDTO newUserPassword) {
        UserAccount currentAcc = userAccountRepository.findByEmail((newUserPassword.getEmail()));
        if(currentAcc != null) {
            currentAcc.setPassword(encoder.encode(newUserPassword.getPassword()));
            userAccountRepository.save(currentAcc);
            return "Password changed";
        }else{
            return null;
        }
    }
}
