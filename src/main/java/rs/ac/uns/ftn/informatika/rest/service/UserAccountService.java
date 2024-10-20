package rs.ac.uns.ftn.informatika.rest.service;

import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;

import java.util.Collection;

public interface UserAccountService {

    UserAccount create(UserAccountDTO userAccountDto) throws Exception;
    Collection<UserAccount> findAll();
    UserAccount findById(Long id);
    UserAccount update(UserAccountDTO userAccountDto, Long id) throws Exception;
    UserAccount delete(Long id);

}
