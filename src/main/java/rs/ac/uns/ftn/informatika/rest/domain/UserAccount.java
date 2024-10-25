package rs.ac.uns.ftn.informatika.rest.domain;


import jakarta.persistence.*;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;

/*import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;*/

@Entity
@Table(name = "UserAccounts")
public class UserAccount {

    @Id
    @Column(name = "acc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "address")
    private String address;
    @Column(name = "followers_count")
    private int followersCount;
    private String role;

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

    public UserAccount(Long id, String firstName, String lastName, String email, String password, String address, int followersCount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.followersCount = followersCount;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
