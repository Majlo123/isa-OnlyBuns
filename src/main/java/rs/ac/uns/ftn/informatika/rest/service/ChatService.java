package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.Chat;
import rs.ac.uns.ftn.informatika.rest.domain.Message;
import rs.ac.uns.ftn.informatika.rest.repository.ChatRepository;
import rs.ac.uns.ftn.informatika.rest.repository.MessageRepository;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<Chat> getChatsByUserId(Long userId) {
        return chatRepository.findByParticipantsContaining(userId);
    }

    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatIdOrderByTimestampAsc(chatId);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }
}
