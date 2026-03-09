package com.fleetmanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "company_bank")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The 20% pot
    @Column(nullable = false)
    private BigDecimal totalCompanyProfit = BigDecimal.ZERO;

    // The 10% pot
    @Column(nullable = false)
    private BigDecimal totalMaintenanceFund = BigDecimal.ZERO;

    // Lifetime total revenue (all money that passed through)
    @Column(nullable = false)
    private BigDecimal lifetimeRevenue = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    @PreUpdate
    @PrePersist
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }
}