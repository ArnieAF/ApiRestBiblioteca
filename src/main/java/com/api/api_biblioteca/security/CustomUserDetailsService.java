package com.api.api_biblioteca.security;

import com.api.api_biblioteca.persistence.crud.UsuarioCrudRepository;
import com.api.api_biblioteca.persistence.entity.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioCrudRepository usuarioCrudRepository;

    public CustomUserDetailsService(UsuarioCrudRepository usuarioCrudRepository) {
        this.usuarioCrudRepository = usuarioCrudRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioCrudRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole()))
        );
    }
}