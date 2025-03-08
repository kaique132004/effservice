package com.effservice.server.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Carregar os detalhes do usuário a partir do banco de dados
        // Certifique-se de que a senha retornada está codificada com BCrypt
        return new org.springframework.security.core.userdetails.User("user", "$2a$12$QB2bukKPWXOFaPQZDBGXCO7YSAgwvTpCGQPOtVeHQ.N2TLLCvO0Im", new ArrayList<>());
    }
}