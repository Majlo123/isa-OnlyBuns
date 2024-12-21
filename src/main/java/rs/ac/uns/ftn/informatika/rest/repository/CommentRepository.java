package rs.ac.uns.ftn.informatika.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;

import java.util.Date;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    long countCommentsBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}