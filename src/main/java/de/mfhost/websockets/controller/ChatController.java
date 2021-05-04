package de.mfhost.websockets.controller;

import de.mfhost.websockets.models.ChatMessage;
import de.mfhost.websockets.models.ChatNotification;
import de.mfhost.websockets.models.MessageStatus;
import de.mfhost.websockets.service.ChatMessageService;
import de.mfhost.websockets.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private ChatRoomService chatRoomService;


    /**
     * Process Incoming Messages
     * Save message as SEND in mongodb
     * Send {@link ChatNotification} to reciever
     * @param chatMessage sent message
     */
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecieverId(), true);
        chatMessage.setChatId(chatId.get());
        chatMessage.setStatus(MessageStatus.SEND);

        ChatMessage saved = chatMessageService.save(chatMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecieverId(),"/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId()
                )
        );
    }

    /**
     * Endpoint to get new (unread) messages from a specific user
     * @param senderId
     * @param recipientId
     * @return
     */
    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {

        return ResponseEntity
                .ok(chatMessageService.countNewMessages(senderId, recipientId));
    }

    /**
     * Endpoint to get all chatMessages from a specific chat
     * @param senderId
     * @param recipientId
     * @return
     */
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages ( @PathVariable String senderId,
                                                @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    /**
     * Endpoint to get specific message
     * @param id
     * @return
     */
    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage ( @PathVariable String id) {
        return ResponseEntity
                .ok(chatMessageService.findById(id));
    }

    /**
     * Endpoint to
     * @param id
     * @return
     */
    @GetMapping("/messages/{id}/read")
    public ResponseEntity<?> readMessage(@PathVariable String id) {
        return ResponseEntity.
                ok(chatMessageService.updateStatus(id, MessageStatus.READED));
        //TODO notify changes
    }

}
