package rs.ac.uns.ftn.informatika.rest.service;

import org.apache.catalina.User;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.domain.UserInfo;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;

public interface ChangeUserInfoService {

    UserAccount changeFirstAndLastName(UserInfo newUserInfo);
    UserAccount changeAddress(UserInfo newUserInfo);
    String changePassword(UserAccountDTO newUserPassword);

}
