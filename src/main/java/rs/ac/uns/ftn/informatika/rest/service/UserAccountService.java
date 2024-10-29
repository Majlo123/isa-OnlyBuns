package rs.ac.uns.ftn.informatika.rest.service;

import jakarta.servlet.http.HttpServletRequest;
import rs.ac.uns.ftn.informatika.rest.domain.AuthRequest;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;

import java.util.Collection;

public interface UserAccountService {

    UserAccount create(UserAccountDTO userAccountDto, HttpServletRequest request) throws Exception;
    Collection<UserAccount> findAll();
    UserAccount findById(Long id);
    UserAccount update(UserAccountDTO userAccountDto, Long id) throws Exception;
    UserAccount delete(Long id);
    String verify(AuthRequest authRequest);
    void sendVerificationEmail(UserAccount savedAcc, HttpServletRequest request) throws Exception;
    boolean verifyVerificationCode(String verificationCode);
}
