package rs.ac.uns.ftn.informatika.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.informatika.rest.domain.Follow;

import java.time.LocalDateTime;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followerId = :followerId AND f.followedAt >= :startTime")
    long countFollowsInLastMinute(Long followerId, LocalDateTime startTime);
    @Query("SELECT f.followeeId FROM Follow f WHERE f.followerId = :followerId")
    List<Long> findFolloweeIdsByFollowerId(Long followerId);
    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
