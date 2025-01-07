package rs.ac.uns.ftn.informatika.rest.service;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.Chat;
import rs.ac.uns.ftn.informatika.rest.domain.JoinRecord;
import rs.ac.uns.ftn.informatika.rest.domain.Message;
import rs.ac.uns.ftn.informatika.rest.repository.ChatRepository;
import rs.ac.uns.ftn.informatika.rest.repository.JoinRecordRepository;
import rs.ac.uns.ftn.informatika.rest.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private JoinRecordRepository joinRecordRepository;
    @Autowired
    private MessageRepository messageRepository;


    public List<Chat> getChatsByUserId(Long userId) {
        return chatRepository.findByParticipantsContaining(userId);
    }

    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Chat addUserToGroup(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
        chat.getParticipants().add(userId);
        return chatRepository.save(chat);
    }
    public boolean isNewMember(Long chatId, Long userId) {
        return !joinRecordRepository.findByChatIdAndUserId(chatId, userId).isPresent();
    }

    public void saveJoinRecord(Long chatId, Long userId) {
        JoinRecord joinRecord = new JoinRecord(chatId, userId, LocalDateTime.now());
        joinRecordRepository.save(joinRecord);
    }

    public List<Message> getRecentMessages(Long chatId, int count) {
        Pageable pageable = PageRequest.of(0, count);
        return messageRepository.findByChatIdOrderByTimestampDesc(chatId, pageable).getContent();
    }

    public List<Message> getMessagesSinceJoin(Long chatId, Long userId) {
        var joinRecord = joinRecordRepository.findByChatIdAndUserId(chatId, userId);
        if (joinRecord.isPresent()) {
            LocalDateTime joinTime = joinRecord.get().getJoinTime();
            return messageRepository.findByChatIdAndTimestampAfterOrderByTimestampAsc(chatId, joinTime);
        } else {
            // Ako nema join record, vrati poslednjih 10 poruka
            return getRecentMessages(chatId, 10);
        }
    }
    public Chat removeUserFromGroup(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
        chat.getParticipants().remove(userId);
        return chatRepository.save(chat);
    }

    // U ChatService:
    public List<Message> getMessagesPaged(Long chatId, int page, int size) {
        // Možeš koristiti PageRequest i naručiti poruke po datumu opadajuće ili rastuće.
        // Na primer, najnovije prve:
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

        // Pretpostavimo da si u repositorijumu definisao:
        // Page<Message> findByChatId(Long chatId, Pageable pageable);
        Page<Message> pageResult = messageRepository.findByChatId(chatId, pageable);

        // Vrati listu
        return pageResult.getContent();
    }


    public Message saveMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }
    public List<Message> getLatestMessagesByChatId(Long chatId) {
        return messageRepository.findByChatIdOrderByTimestampAsc(chatId); // Pronalazi poslednjih 10
    }


    public Chat getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found with id: " + id));
    }

}

