package de.mfhost.websockets.service;

import de.mfhost.websockets.models.ChatRoom;
import de.mfhost.websockets.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatId(String senderId, String recieverId, boolean createIfNotExist){
        return chatRoomRepository
                .findBySenderIdAndRecieverId(senderId, recieverId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(!createIfNotExist) {
                        return Optional.empty();
                    }
                    var chatId = String.format("%s_%s", senderId, recieverId);

                    ChatRoom senderReciever = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recieverId(recieverId)
                            .build();

                    ChatRoom recieverSender = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(recieverId)
                            .recieverId(senderId)
                            .build();
                    chatRoomRepository.save(senderReciever);
                    chatRoomRepository.save(recieverSender);

                    return Optional.of(chatId);
                });
    }


}
