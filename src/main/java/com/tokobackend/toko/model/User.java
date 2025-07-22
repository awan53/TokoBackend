package com.tokobackend.toko.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY untuk SQL Server (auto-increment)
    private Long id;

    @Column(name = "nm_user", nullable = false)
    private String nmUser;

    @Column(name = "USR_NAME", nullable = false, unique = true)
    private String username;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Column(name = "alamat", nullable = false)
    private String alamat;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String PhNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();



    // Konstruktor, Getter, dan Setter
    public User(){
    }

    public User(String nmUser, String username, LocalDate tanggalLahir, String alamat, String email, String password, String phoneNumber) {
        this.nmUser = nmUser;
        this.username = username;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.email = email;
        this.password = password;
        this.PhNumber =phoneNumber;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNmUser() {
        return nmUser;
    }

    public void setNmUser(String nmUser) {
        this.nmUser = nmUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhNumber() {
        return PhNumber;
    }

    public void setPhNumber(String phNumber) {
        PhNumber = phNumber;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void addOrder(Order order){
        this.orders.add(order);
        order.setUser(this);
    }

    public void removeOrder(Order order)
    {
        this.orders.remove(order);
        order.setUser(null);
    }

    public void addCartItem(CartItem cartItem){
        this.cartItems.add(cartItem);
        cartItem.setUser(this);
    }

    public void removeCartItem(CartItem cartItem){
        this.cartItems.add(cartItem);
        cartItem.setUser(null);
    }
}
