package de.mfhost.websockets.service;

import de.mfhost.websockets.exceptions.ResourceNotFoundException;
import de.mfhost.websockets.models.ChatMessage;
import de.mfhost.websockets.models.MessageStatus;
import de.mfhost.websockets.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository repository;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private MongoOperations mongoOperations;


    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    /**
     * Counts all messages in a chat, that are SEND but not RECIEVED yet
     * @param senderId
     * @param recipientId
     * @return count of not recieved messages
     */
    public long countNewMessages(String senderId, String recipientId) {
        return repository.countBySenderIdAndRecieverIdAndStatus(
                senderId, recipientId, MessageStatus.SEND);
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        var messages =
                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());

        if(messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.RECEIVED);
        }

        return messages;
    }

    public ChatMessage findById(String id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.RECEIVED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }


    public boolean updateStatus(String id, MessageStatus status) {
        Query query = new Query(
                Criteria.where("id").is(id)
        );
        Update update = Update.update("status", status);
        mongoOperations.updateFirst(query, update, ChatMessage.class);
        return true;
    }

    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        Query query = new Query(
                Criteria
                        .where("senderId").is(senderId)
                        .and("recipientId").is(recipientId));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, ChatMessage.class);
    }

}
