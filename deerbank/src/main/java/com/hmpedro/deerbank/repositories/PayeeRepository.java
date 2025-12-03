package com.hmpedro.deerbank.repositories;

import com.hmpedro.deerbank.entities.Payee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayeeRepository extends JpaRepository<Payee, Long> {
}
