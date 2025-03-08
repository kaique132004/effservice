package com.effservice.server.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService {
     private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void createUser(String username, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        // Salve o usuário com a senha codificada no banco de dados
    }
}
