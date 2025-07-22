package com.tokobackend.toko.controller;
import com.tokobackend.toko.payload.JwtResponse;
import com.tokobackend.toko.payload.LoginRequest;
import com.tokobackend.toko.payload.RegisterRequest;
import com.tokobackend.toko.payload.response.MessageRespone;

import com.tokobackend.toko.model.User;
import com.tokobackend.toko.model.Role; // Import model Role Anda
import com.tokobackend.toko.model.ERole; // Import enum ERole Anda
import com.tokobackend.toko.repository.UserRepository;
import com.tokobackend.toko.repository.RoleRepository; // Import RoleRepository
import com.tokobackend.toko.security.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600) // Mengizinkan CORS dari semua origin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository; // <-- Telah ditambahkan Autowired

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    // userDetailsService tidak wajib di-autowire di sini jika hanya untuk memuat user details dari database
    // selama UserDetailsServiceImpl Anda diimplementasikan dengan benar untuk security chain.
    // @Autowired
    // UserDetailsServiceImpl userDetailsService;

    // --- Endpoint untuk Login ---
    @PostMapping("/signin") // URL /api/auth/signin
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Mengembalikan JwtResponse dengan token dan detail user
        return ResponseEntity.ok(new JwtResponse(jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles)); // <-- Gunakan variabel 'roles' (List<String>)
    }

    // --- Endpoint untuk Registrasi ---
    @PostMapping("/register") // URL /api/auth/register
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageRespone("Error: Username is already taken!")); // <-- Diperbaiki ke MessageResponse
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageRespone("Error: Email is already in use!")); // <-- Diperbaiki ke MessageResponse
        }

        User user = new User(
                registerRequest.getNmUser(),
                registerRequest.getUsername(),
                registerRequest.getTanggalLahir(),
                registerRequest.getAlamat(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getPhNumber()
        );

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR) // <-- PERBAIKAN PENTING DI SINI
                                .orElseThrow(() -> new RuntimeException("Error: Role MODERATOR is not found."));
                        roles.add(modRole);
                        break;
                    case "user":
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageRespone("User registered successfully!")); // <-- Diperbaiki ke MessageResponse
    }
}
