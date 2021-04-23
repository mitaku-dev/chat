package de.mfhost.websockets.repository;

import de.mfhost.websockets.models.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    Optional<ChatRoom> findBySenderIdAndRecieverId(String senderId, String recieverId);

}
