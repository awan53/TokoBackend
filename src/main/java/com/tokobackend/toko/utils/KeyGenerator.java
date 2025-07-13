package com.tokobackend.toko.utils;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        // Menghasilkan kunci yang aman untuk algoritma HS256 (256 bits)
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String secretString = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Salin ini untuk toko.app.jwtSecret: " + secretString);
    }
}
