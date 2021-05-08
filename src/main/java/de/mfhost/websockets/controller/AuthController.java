package de.mfhost.websockets.controller;

import de.mfhost.websockets.models.User;
import de.mfhost.websockets.security.AuthRequest;
import de.mfhost.websockets.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@Api(tags = "Authentication")
@Controller
public class AuthController {

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody AuthRequest request) {
        try {


            Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getName(),
                      request.getPassword()
              )
            );

            User user = (User) authentication.getPrincipal();

            return ResponseEntity.ok().
                    header(
                            HttpHeaders.AUTHORIZATION,
                            jwtTokenUtil.generateAccessToken(user)
                    ).body(user);
        } catch(BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
