package com.tokobackend.toko.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;
import java.util.Set;


public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 3, max = 20)
    private String password;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    private Set<String> roles;
    private String nmUser; // Sesuai dengan nm_user di model User
    private LocalDate tanggalLahir; // Sesuai dengan tanggal_lahir di model User
    private String alamat;   // Sesuai dengan alamat di model User
    private String phNumber;

    // Constructors
    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String email, Set<String> roles, String nmUser, LocalDate tanggalLahir, String alamat, String phNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
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
