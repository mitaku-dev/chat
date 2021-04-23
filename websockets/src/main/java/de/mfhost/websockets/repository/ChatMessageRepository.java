package de.mfhost.websockets.repository;

import de.mfhost.websockets.models.ChatMessage;
import de.mfhost.websockets.models.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    long countBySenderIdAndRecieverIdAndStatus(
            String senderId, String recieverId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);
}
