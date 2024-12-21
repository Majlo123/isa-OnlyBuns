package rs.ac.uns.ftn.informatika.rest.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.rest.domain.Post;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments c WHERE p.deleted = false ORDER BY c.createdAt DESC")
    List<Post> findAllPostsWithSortedComments();

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments c WHERE p.deleted = false and p.userId = :userId ORDER BY c.createdAt DESC")
    List<Post> findAllByUserIdAndDeletedFalse(Long userId);
    List<Post> findByUserIdIn(List<Long> userIds);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Post p WHERE p.id = :postId")
    Optional<Post> findByIdWithLock(@Param("postId") Long postId);
    @Query("SELECT COUNT(p) FROM Post p WHERE p.dateOfCreation BETWEEN :startDate AND :endDate")
    long countPostsBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.userId = :userId AND c.createdAt >= :oneHourAgo")
    int countCommentsInLastHour(@Param("userId") Long userId, @Param("oneHourAgo") LocalDateTime oneHourAgo);
}