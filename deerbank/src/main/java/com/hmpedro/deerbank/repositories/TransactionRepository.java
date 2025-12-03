package com.hmpedro.deerbank.repositories;

import com.hmpedro.deerbank.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
           SELECT t 
           FROM Transaction t
           JOIN t.account a
           JOIN a.owner u
           WHERE u.id = :userId
           ORDER BY t.createdAt DESC
           """)
    List<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId);
}
