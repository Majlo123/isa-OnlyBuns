package rs.ac.uns.ftn.informatika.rest.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.rest.domain.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatIdOrderByTimestampAsc(Long chatId);
    List<Message> findTop10ByChatIdOrderByTimestampDesc(Long chatId);
    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.timestamp DESC")
    Page<Message> findByChatId(@Param("chatId") Long chatId, Pageable pageable);
    List<Message> findTop10ByChatIdAndTimestampAfterOrderByTimestampAsc(Long chatId, LocalDateTime timestamp);
    Page<Message> findByChatIdAndTimestampAfter(Long chatId, LocalDateTime timestamp, Pageable pageable);

}
