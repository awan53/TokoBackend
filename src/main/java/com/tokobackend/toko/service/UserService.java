package com.tokobackend.toko.service;

import com.tokobackend.toko.model.User;
import com.tokobackend.toko.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User userDetails){
        return userRepository.findById(id).map(user -> {user.setNmUser(userDetails.getNmUser());
        user.setUsername(userDetails.getUsername());
        user.setTanggalLahir(userDetails.getTanggalLahir());
        user.setAlamat(userDetails.getAlamat());
        user.setEmail(userDetails.getEmail());
        user.setPhNumber(userDetails.getPhNumber());
        user.setRole(userDetails.getRole());
        return userRepository.save(user);
        }).orElseThrow(()-> new RuntimeException("User tidak ditemukan "+id));
    }


}
