package de.mfhost.websockets.Config;

import de.mfhost.websockets.repository.UserRepository;
import de.mfhost.websockets.security.JwtTokenUtil;
import de.mfhost.websockets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class WebSocketSecurity implements ChannelInterceptor {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;


    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader("Authorization");
            //final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER);
            token = token.split(" ")[1].trim();
            String id = jwtTokenUtil.getUserId(token); //TODO BEARER
            final UsernamePasswordAuthenticationToken user =
                    new UsernamePasswordAuthenticationToken(
                            userRepository.findById(id).get().getUsername(),
                            null,
                            Collections.singleton((GrantedAuthority) () -> "USER")
                    );

            accessor.setUser(user);
        }
        return message;
    }

}
