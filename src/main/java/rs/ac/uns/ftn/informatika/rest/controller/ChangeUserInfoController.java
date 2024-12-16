package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.rest.domain.Address;
import rs.ac.uns.ftn.informatika.rest.domain.AuthRequest;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.domain.UserInfo;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;
import rs.ac.uns.ftn.informatika.rest.service.ChangeUserInfoService;

@RestController
@RequestMapping("/api/changeUserInfo")
public class ChangeUserInfoController {
    @Autowired
    private ChangeUserInfoService changeUserInfoService;

    @PutMapping
    public ResponseEntity<String> changeFirstName(@RequestBody UserInfo newUserInfo) {
        UserAccount updated = changeUserInfoService.changeFirstAndLastName(newUserInfo);
        if(updated != null){
            String updatedFirstName = updated.getFirstName();
            return ResponseEntity.ok(updatedFirstName);

        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(path = "/address")
    public ResponseEntity<Address> changeAddress(@RequestBody UserInfo newUserInfo) {
        UserAccount updated = changeUserInfoService.changeAddress(newUserInfo);
        if(updated != null){
            Address updatedAddress = updated.convertJsonToAddress();
            return ResponseEntity.ok(updatedAddress);

        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(path = "/password")
    public ResponseEntity<String> changePassword(@RequestBody UserAccountDTO newUserPassword) {
        String updated = changeUserInfoService.changePassword(newUserPassword);
        if(updated != null){

            return ResponseEntity.ok("Successfully changed");

        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


}
