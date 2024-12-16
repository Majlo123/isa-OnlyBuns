package rs.ac.uns.ftn.informatika.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.rest.domain.Likes;
import rs.ac.uns.ftn.informatika.rest.domain.Post;

public interface LikesRepository extends JpaRepository<Likes, Long> {

}
