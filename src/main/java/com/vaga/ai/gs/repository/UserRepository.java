package com.vaga.ai.gs.repository;

import com.vaga.ai.gs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    // SELECT count(*) > 0 FROM USUARIOS WHERE email = ?
    boolean existsByEmail(String email);

    UserDetails findByEmail(String email);
}
