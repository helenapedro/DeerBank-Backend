package com.hmpedro.deerbank.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Owner of payee (customer)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 100)
    private String category;

    @Column(name = "external_account_ref", length = 100)
    private String externalAccountRef;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "payee", fetch = FetchType.LAZY)
    private List<RecurringPayment> recurringPayments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "payee", fetch = FetchType.LAZY)
    private List<BillPayment> billPayments = new ArrayList<>();
}
