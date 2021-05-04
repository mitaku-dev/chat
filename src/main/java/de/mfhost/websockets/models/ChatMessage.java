package de.mfhost.websockets.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatMessage {

    @Id
    private String id;
    private String senderId;
    private String recieverId;
    private String chatId;
    private Date timestamp;
    private MessageStatus status;
    private String message;

}


