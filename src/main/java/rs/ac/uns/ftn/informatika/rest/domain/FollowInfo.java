package rs.ac.uns.ftn.informatika.rest.domain;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name ="FollowInfo")
public class FollowInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userAccountId")
    private Long userAccountId;

    @Column(name = "followedById")
    private Long followedById;

    @Column(name = "followDate")
    private Date followDate;

    public FollowInfo() {}

    public FollowInfo(Long userAccountId, Long followedById, Date followDate) {
        this.userAccountId = userAccountId;
        this.followedById = followedById;
        this.followDate = followDate;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }
    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Long getFollowedById() {
        return followedById;
    }
    public void setFollowedById(Long followedById) {
        this.followedById = followedById;
    }

    public Date getFollowDate() {
        return followDate;
    }
    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }


}
