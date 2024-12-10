package rs.ac.uns.ftn.informatika.rest.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    public Comment() {}

    public Comment(String content, Long userId, LocalDateTime createdAt) {
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // Getter i setter za content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter i setter za userId
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt;}

}
