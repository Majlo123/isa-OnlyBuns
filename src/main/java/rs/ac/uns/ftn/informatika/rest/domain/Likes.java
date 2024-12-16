package rs.ac.uns.ftn.informatika.rest.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long like_id;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "acc_id", nullable = false)
    private UserAccount user;

    @Column(nullable = false)
    private LocalDateTime dateLiked;

    public Long getId() {
        return like_id;
    }

    public void setId(Long id) {
        this.like_id = like_id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public LocalDateTime getDateLiked() {
        return dateLiked;
    }

    public void setDateLiked(LocalDateTime dateLiked) {
        this.dateLiked = dateLiked;
    }
}
