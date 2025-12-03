package com.hmpedro.deerbank.repositories;

import com.hmpedro.deerbank.entities.RecurringPayment;
import com.hmpedro.deerbank.entities.RecurringPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecurringPaymentRepository extends JpaRepository<RecurringPayment, Long> {
    List<RecurringPayment> findByStatus(RecurringPaymentStatus status);
}
