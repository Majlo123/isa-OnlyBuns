package rs.ac.uns.ftn.informatika.rest.domain;

import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class UserAccount {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String address;

    private int followersCount;

    public UserAccount() {

    }
    public UserAccount(UserAccountDTO userAccountDTO){
        this.address = userAccountDTO.getAddress();
        this.firstName = userAccountDTO.getFirstName();
        this.lastName = userAccountDTO.getLastName();

        this.email = userAccountDTO.getEmail();
        this.password = userAccountDTO.getPassword();
        this.followersCount = userAccountDTO.getFollowersCount();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }
}
