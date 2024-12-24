package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.Chat;
import rs.ac.uns.ftn.informatika.rest.domain.Message;
import rs.ac.uns.ftn.informatika.rest.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/user/{userId}")
    public List<Chat> getChatsByUserId(@PathVariable Long userId) {
        return chatService.getChatsByUserId(userId);
    }

    @PostMapping
    public Chat createChat(@RequestBody Chat chat) {
        return chatService.createChat(chat);
    }

    @PostMapping("/{chatId}/addUser")
    public Chat addUserToGroup(@PathVariable Long chatId, @RequestParam Long userId) {
        return chatService.addUserToGroup(chatId, userId);
    }

    @DeleteMapping("/{chatId}/removeUser")
    public Chat removeUserFromGroup(@PathVariable Long chatId, @RequestParam Long userId) {
        return chatService.removeUserFromGroup(chatId, userId);
    }

    @GetMapping("/{chatId}/messages")
    public List<Message> getMessagesByChatId(@PathVariable Long chatId) {
        return chatService.getMessagesByChatId(chatId);
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(Message message) {
        return chatService.saveMessage(message);
    }
}
