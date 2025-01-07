package rs.ac.uns.ftn.informatika.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.rest.domain.JoinRecord;

import java.util.Optional;

public interface JoinRecordRepository extends JpaRepository<JoinRecord, Long> {
    Optional<JoinRecord> findByChatIdAndUserId(Long chatId, Long userId);
}
