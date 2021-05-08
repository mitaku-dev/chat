package de.mfhost.websockets.controller;

import de.mfhost.websockets.exceptions.ResourceNotFoundException;
import de.mfhost.websockets.models.User;
import de.mfhost.websockets.repository.UserRepository;
import de.mfhost.websockets.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id){
        Optional<User> user = userRepository.findById(id);
        return ResponseEntity.ok(user.orElseThrow(() -> new ResourceNotFoundException(String.format("user with id %s not found",id))));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){


        //username dosn't exist yet
        if (userRepository.findByName(user.getName()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        //TODO validate password

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User responseUser = userRepository.save(user);
        return ResponseEntity.ok().header(
                HttpHeaders.AUTHORIZATION,
                jwtTokenUtil.generateAccessToken(user)
        ).body(responseUser);
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> searchUser(@RequestParam Optional<String> q){
        Iterable<User> response;
        if(q.isPresent()) response = userRepository.findByNameStartsWith(q.get());
        else response = userRepository.findAll();

        return ResponseEntity.ok(response);
    }

}
