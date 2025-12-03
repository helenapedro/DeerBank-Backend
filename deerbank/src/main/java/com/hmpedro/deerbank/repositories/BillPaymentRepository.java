package com.hmpedro.deerbank.repositories;

import com.hmpedro.deerbank.entities.BillPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
}
