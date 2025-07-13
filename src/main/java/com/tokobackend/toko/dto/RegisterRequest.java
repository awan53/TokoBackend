package com.tokobackend.toko.dto;

import java.time.LocalDate;


public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String role;
    private String nmUser; // Sesuai dengan nm_user di model User
    private LocalDate tanggalLahir; // Sesuai dengan tanggal_lahir di model User
    private String alamat;   // Sesuai dengan alamat di model User
    private String phNumber;

    // Constructors
    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String email, String role, String nmUser, LocalDate tanggalLahir, String alamat, String phNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.nmUser = nmUser;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.phNumber = phNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNmUser() {
        return nmUser;
    }

    public void setNmUser(String nmUser) {
        this.nmUser = nmUser;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }
}
