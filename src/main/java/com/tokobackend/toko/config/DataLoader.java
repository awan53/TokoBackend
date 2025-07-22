package com.tokobackend.toko.config;
import com.tokobackend.toko.model.Role;
import com.tokobackend.toko.model.ERole;
import com.tokobackend.toko.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception{
        if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_USER));
            System.out.println("Role ROLE_USER ditambahkan.");
        }
        if (roleRepository.findByName(ERole.ROLE_MODERATOR).isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_MODERATOR));
            System.out.println("Role ROLE_MODERATOR ditambahkan.");
        }
        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
            System.out.println("Role ROLE_ADMIN ditambahkan.");
        }
    }

}
