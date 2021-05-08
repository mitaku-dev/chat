package de.mfhost.websockets.security;

import lombok.Data;

@Data
public class AuthRequest {

    String name;
    String password;

}
