package com.example.carboncredit.service.auth;

import com.example.carboncredit.entity.User;
import com.example.carboncredit.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        var authority = new SimpleGrantedAuthority("ROLE_" + u.getRole());
        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getEmail())
                .password(u.getPasswordHash() == null ? "" : u.getPasswordHash())
                .authorities(List.of(authority))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!Boolean.TRUE.equals(u.getEnabled()))
                .build();
    }
}
