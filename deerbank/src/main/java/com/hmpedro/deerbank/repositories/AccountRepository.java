package com.hmpedro.deerbank.repositories;

import com.hmpedro.deerbank.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
