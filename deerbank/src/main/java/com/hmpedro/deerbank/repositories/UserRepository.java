package com.hmpedro.deerbank.repositories;

import com.hmpedro.deerbank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
