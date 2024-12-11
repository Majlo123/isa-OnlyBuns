package rs.ac.uns.ftn.informatika.rest.dto;

import java.util.Date;

public class FollowInfoDTO {

    private Long userAccountId;
    private Long followedById;
    private Date followedDate;

    public FollowInfoDTO() {}

    public FollowInfoDTO(Long userAccountId, Long followedById, Date followedDate) {
        this.userAccountId = userAccountId;
        this.followedById = followedById;
        this.followedDate = followedDate;
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
    public Date getFollowedDate() {
        return followedDate;
    }
    public void setFollowedDate(Date followedDate) {
        this.followedDate = followedDate;
    }
}
