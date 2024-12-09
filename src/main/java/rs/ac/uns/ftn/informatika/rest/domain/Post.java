package rs.ac.uns.ftn.informatika.rest.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    private String title;
    private String imageUrl;
    private int likes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "user_id", nullable = false)
    private Long userId;  // Novo polje za povezivanje sa korisnikom

    private int longitude;
    private int latitude;
    private LocalDateTime dateOfCreation;

    // Konstruktor, getteri i setteri
    public Post() {
        this.title = "";
        this.description = "";
        this.imageUrl = "";
        this.likes = 0;
        this.comments = new ArrayList<>();
        this.userId = null;
        this.longitude = 0;
        this.latitude = 0;
        this.dateOfCreation = LocalDateTime.now();
        this.deleted = false;
    }

    public Post(String title, String description, String imageUrl, Long userId, int longitude, int latitude, LocalDateTime dateOfCreation) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.likes = 0;
        this.deleted = false;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dateOfCreation = dateOfCreation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getLongitude() {
        return this.longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return this.latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public LocalDateTime getDateOfCreation(){
        return this.dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation){
        this.dateOfCreation = dateOfCreation;
    }


}
