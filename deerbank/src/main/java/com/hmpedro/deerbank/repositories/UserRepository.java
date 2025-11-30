package com.hmpedro.deerbank.repositories;

import com.hmpedro.deerbank.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"accounts"})
    Optional<User> findWithAccountsById(Long id);

    @EntityGraph(attributePaths = {"accounts", "payees"})
    Optional<User> findWithAccountsAndPayeesById(Long id);

}
