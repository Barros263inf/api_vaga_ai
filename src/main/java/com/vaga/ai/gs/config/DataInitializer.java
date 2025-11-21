package com.vaga.ai.gs.config;

import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.model.enums.Role;
import com.vaga.ai.gs.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        String emailAdmin = "admin@vaga.ai";

        if (!userRepository.existsByEmail(emailAdmin)) {
            User admin = new User();
            admin.setName("Administrador Padrão");
            admin.setEmail(emailAdmin);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("11999990000");
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("---------------------------------");
            System.out.println("✅ USER ADMIN CRIADO COM SUCESSO");
            System.out.println("Email: " + emailAdmin);
            System.out.println("Senha: admin123");
            System.out.println("---------------------------------");
        }
    }
}