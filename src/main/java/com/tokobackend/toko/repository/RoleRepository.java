package com.tokobackend.toko.repository;
import com.tokobackend.toko.model.ERole; // Import enum ERole
import com.tokobackend.toko.model.Role;   // Import model Role
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);
}
