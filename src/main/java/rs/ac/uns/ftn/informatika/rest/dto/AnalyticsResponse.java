package rs.ac.uns.ftn.informatika.rest.dto;

public class AnalyticsResponse {

    private long postCount; // Broj objava
    private long commentCount; // Broj komentara
    private long totalUsers; // Ukupan broj korisnika
    private long usersWithPosts; // Korisnici sa objavama
    private long usersWithComments; // Korisnici sa komentarima

    // Procenti
    private double postingPercentage; // Procenat korisnika sa objavama
    private double commentingPercentage; // Procenat korisnika sa komentarima
    private double inactivePercentage;
    // Konstruktor
    public AnalyticsResponse(
            long postCount,
            long commentCount,
            long totalUsers,
            long usersWithPosts,
            long usersWithComments,
            double postingPercentage,
            double commentingPercentage,
            double inactivePercentage
    ) {
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.totalUsers = totalUsers;
        this.usersWithPosts = usersWithPosts;
        this.usersWithComments = usersWithComments;
        this.postingPercentage = postingPercentage;
        this.commentingPercentage = commentingPercentage;
        this.inactivePercentage = inactivePercentage;
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
    public double getInactivePercentage() { return inactivePercentage; }
    public long getUsersWithPosts() {
        return usersWithPosts;
    }

    public long getUsersWithComments() {
        return usersWithComments;
    }

    public double getPostingPercentage() {
        return postingPercentage;
    }

    public double getCommentingPercentage() {
        return commentingPercentage;
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

    public void setPostingPercentage(double postingPercentage) {
        this.postingPercentage = postingPercentage;
    }

    public void setCommentingPercentage(double commentingPercentage) {
        this.commentingPercentage = commentingPercentage;
    }
}
