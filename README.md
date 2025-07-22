# 🛒 Backend Aplikasi Toko (Spring Boot) on going

Ini adalah **proyek backend-only** untuk aplikasi toko sederhana. Backend ini dibangun menggunakan **Spring Boot** dan saat ini belum memiliki frontend. Cocok digunakan sebagai dasar pengembangan sistem e-commerce atau manajemen inventaris.

---

## ✨ Fitur yang Tersedia

- ✅ **Manajemen Pengguna**
  - Registrasi user biasa
  - Registrasi atau pembuatan user dengan role `ADMIN`
  - Autentikasi (login) berbasis role (user/admin)

- ✅ **Manajemen Produk dan Kategori**
  - Tambah kategori (khusus admin)
  - Tambah produk (khusus admin)

---

## 🛠 Teknologi yang Digunakan

- Java 17
- Spring Boot
- Spring Security
- JPA (Hibernate)
- Database: SQL Server

---

## 📦 Struktur Proyek 
com.tokobackend.toko
├── config # Konfigurasi aplikasi dan keamanan
├── controller # REST API endpoint
├── model # Model entitas (User, Product, Category, dll)
├── payload # DTO (Request/Response Payloads)
├── repository # Interaksi dengan database (JPA Repository)
├── response # Struktur response standar
├── security # Konfigurasi keamanan, JWT, dll
├── service # Logika bisnis utama
├── utilis # Utilitas umum (helper, converter, dll)


