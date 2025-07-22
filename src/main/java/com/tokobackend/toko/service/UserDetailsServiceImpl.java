package com.tokobackend.toko.service;
import com.tokobackend.toko.model.User;
import com.tokobackend.toko.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements  UserDetailsService  {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // Buat daftar GrantedAuthority dari role user
       List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

        // Return UserDetails yang dibangun dari user kita
        return  new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), authorities);
    }
}
