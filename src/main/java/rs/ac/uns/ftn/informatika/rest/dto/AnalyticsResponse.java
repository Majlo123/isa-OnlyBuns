package rs.ac.uns.ftn.informatika.rest.dto;

public class AnalyticsResponse {
    private long postCount;
    private long commentCount;
    private long totalUsers;
    private long usersWithPosts;
    private long usersWithComments;
    private long inactiveUsers;

    // Konstruktor
    public AnalyticsResponse(long postCount, long commentCount, long totalUsers, long usersWithPosts, long usersWithComments, long inactiveUsers) {
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.totalUsers = totalUsers;
        this.usersWithPosts = usersWithPosts;
        this.usersWithComments = usersWithComments;
        this.inactiveUsers = inactiveUsers;
    }

    // Getteri
    public long getPostCount() {
        return postCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getUsersWithPosts() {
        return usersWithPosts;
    }

    public long getUsersWithComments() {
        return usersWithComments;
    }

    public long getInactiveUsers() {
        return inactiveUsers;
    }

    // Setteri
    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public void setUsersWithPosts(long usersWithPosts) {
        this.usersWithPosts = usersWithPosts;
    }

    public void setUsersWithComments(long usersWithComments) {
        this.usersWithComments = usersWithComments;
    }

    public void setInactiveUsers(long inactiveUsers) {
        this.inactiveUsers = inactiveUsers;
    }
}


