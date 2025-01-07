package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.rest.domain.Chat;
import rs.ac.uns.ftn.informatika.rest.domain.Message;
import rs.ac.uns.ftn.informatika.rest.dto.MessageDTO;
import rs.ac.uns.ftn.informatika.rest.service.ChatService;

import java.time.LocalDateTime;

@RestController
public class WebSocketController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(@RequestBody Message message) {
        if (message.getChat() == null || message.getChat().getId() == null) {
            throw new IllegalArgumentException("Chat ID is required!");
        }

        // Sačuvaj poruku
        Message savedMessage = chatService.saveMessage(message);

        // Pošalji poruku samo u odgovarajući chat
        simpMessagingTemplate.convertAndSend("/topic/messages/" + savedMessage.getChat().getId(), new MessageDTO(savedMessage));
    }

}
