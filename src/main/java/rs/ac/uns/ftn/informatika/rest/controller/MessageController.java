package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.Message;
import rs.ac.uns.ftn.informatika.rest.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        // Timestamp se postavlja u servisu (saveMessage)
        return chatService.saveMessage(message);
    }

    // Endpoint za dobijanje poslednjih 10 poruka
    @GetMapping("/{chatId}/latest")
    public List<Message> getLatestMessages(@PathVariable Long chatId) {
        return chatService.getLatestMessagesByChatId(chatId);
    }
}
