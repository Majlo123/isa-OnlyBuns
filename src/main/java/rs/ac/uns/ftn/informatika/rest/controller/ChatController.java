package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/{chatId}/messages/paged")
    public List<Message> getMessagesPaged(
            @PathVariable Long chatId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return chatService.getMessagesPaged(chatId, page, size);
    }
    @GetMapping("/{chatId}/recent-messages")
    public List<Message> getRecentMessagesForNewMember(
            @PathVariable Long chatId,
            @RequestParam Long userId) {

        // Proveri da li je korisnik novi član
        if (chatService.isNewMember(chatId, userId)) {
            // Ako jeste novi član, sačuvaj vreme pridruživanja
            chatService.saveJoinRecord(chatId, userId);

            // Vrati poslednjih 10 poruka
            return chatService.getRecentMessages(chatId, 10);
        } else {
            // Ako nije novi član, vrati sve poruke od trenutka pridruživanja
            return chatService.getMessagesSinceJoin(chatId, userId);
        }
    }




}
