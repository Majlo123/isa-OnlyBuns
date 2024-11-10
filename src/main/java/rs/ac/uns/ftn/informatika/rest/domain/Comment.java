package rs.ac.uns.ftn.informatika.rest.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Comment() {}

    public Comment(String content, Long userId) {
        this.content = content;
        this.userId = userId;
    }

    // Getteri i setteri uključujući za userId

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}


