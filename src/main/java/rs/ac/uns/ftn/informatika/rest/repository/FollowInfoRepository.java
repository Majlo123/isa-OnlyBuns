package rs.ac.uns.ftn.informatika.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.rest.domain.FollowInfo;

import java.util.List;

public interface FollowInfoRepository  extends JpaRepository<FollowInfo, Long> {

}
