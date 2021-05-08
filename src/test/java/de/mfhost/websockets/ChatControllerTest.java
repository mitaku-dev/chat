package de.mfhost.websockets;

import de.mfhost.websockets.models.ChatMessage;
import de.mfhost.websockets.models.MessageStatus;
import de.mfhost.websockets.service.ChatMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@DataJpaTest
@RunWith(SpringRunner.class)
@SpringBootTest(classes=WebsocketsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerTest {

    @LocalServerPort
    private Integer port;

    WebSocketStompClient webSocketStompClient;
    WebSocketStompClient webSocketStompClient2;

    @Autowired
    ChatMessageService chatMessageService;

    /**
     * Setting Up WebSocket Client
     */
    @BeforeEach
    public void setup(){

        //clear TestDB!

        this.webSocketStompClient = new WebSocketStompClient(
                new SockJsClient(
                        List.of(new WebSocketTransport(new StandardWebSocketClient()))
                )
        );

        this.webSocketStompClient2 = new WebSocketStompClient(
                new SockJsClient(
                        List.of(new WebSocketTransport(new StandardWebSocketClient()))
                )
        );

    }


    @Test
    public void verifyMessageIsProcessed() throws Exception {

        BlockingQueue<ChatMessage> blockingQueue = new ArrayBlockingQueue(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());


        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "Baerer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2MDk2NzM5MDU1ODA5MjdlMmM5OTFmMDEsdGVzdCIsImlzcyI6Im1maG9zdC5kZSIsImlhdCI6MTYyMDQ3ODk0OSwiZXhwIjoxNjIxMDgzNzQ5fQ.jGTdoufAHrJkLxQsYuLc4L6OvHPnk8njVTsVh59VRaAowc-n6v3oOqbSrfS25tWM_ZffOtiylf8q0Eil2HH0PA");

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/ws", port), new WebSocketHttpHeaders(), connectHeaders,  new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);



        ChatMessage message = ChatMessage.builder()
                .timestamp(new Date())
                .message("Hello World!")
                .recieverId("1")
                .senderId("0").build();


        session.send("/app/chat", message);

        //CHat Room 01 get created!
       assertEquals(chatMessageService.countNewMessages("0","1"),1);
    }

}
