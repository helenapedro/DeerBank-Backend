package com.hmpedro.deerbank.config;

import com.hmpedro.deerbank.entities.User;
import com.hmpedro.deerbank.entities.UserRole;
import com.hmpedro.deerbank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TestDataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User newUser = User.builder()
                    .name("Helena Test")
                    .email("helena.test@deerbank.com")
                    .passwordHash("password123") // update to Bcrypt later
                    .role(UserRole.CUSTOMER)
                    .build();
            userRepository.save(newUser);
            System.out.println("✅ Test user created in database!");
        } else {
            System.out.println("✅ Users already exist, skipping test insert.");
        }
    }
}
