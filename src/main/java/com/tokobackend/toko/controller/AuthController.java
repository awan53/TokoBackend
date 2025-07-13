package com.tokobackend.toko.controller;
import com.tokobackend.toko.dto.JwtResponse;
import com.tokobackend.toko.dto.LoginRequest;
import com.tokobackend.toko.dto.RegisterRequest;
import com.tokobackend.toko.model.User;
import com.tokobackend.toko.repository.UserRepository;
import com.tokobackend.toko.security.JwtUtils;
import com.tokobackend.toko.service.UserDetailsServiceImpl; // Pastikan ini diimport

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // Import UserDetails
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid; // Anda mungkin perlu menambahkan dependency validation jika belum ada

@CrossOrigin(origins = "*", maxAge = 3600) // Mengizinkan CORS dari semua origin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService; // Opsional, bisa digunakan untuk memuat detail user setelah autentikasi

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Mengautentikasi user dengan username dan password yang diberikan
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Menempatkan objek autentikasi di SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Menghasilkan token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Mendapatkan UserDetails dari objek autentikasi
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Mengambil entitas User dari database untuk mendapatkan ID, Email, dan Role asli
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        // Mengembalikan JwtResponse dengan token dan detail user
        return ResponseEntity.ok(new JwtResponse(jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole())); // Mengambil role dari entitas User
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        // Validasi apakah username sudah ada
        // >>>>>> BARIS INI YANG HARUS DIUBAH <<<<<<
        if (userRepository.existsByUsername(registerRequest.getUsername())) { // PASTIKAN ADA 's' di 'existsByUsername'
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        // Validasi apakah email sudah ada
        if (userRepository.existsByEmail(registerRequest.getEmail())) { // Ini sudah benar
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        // Membuat objek User baru dengan semua field yang diperlukan oleh constructor di User.java
        // Pastikan registerRequest memiliki semua data ini dan constructor User cocok
        User user = new User(
                registerRequest.getNmUser(),
                registerRequest.getUsername(),
                registerRequest.getTanggalLahir(), // Akan null jika tidak diberikan di request (jika di DB nullable)
                registerRequest.getAlamat(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getPhNumber(),    // Akan null jika tidak diberikan di request (jika di DB nullable)
                registerRequest.getRole() != null ? registerRequest.getRole() : "USER"
        );

        // Simpan user ke database
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }
}
