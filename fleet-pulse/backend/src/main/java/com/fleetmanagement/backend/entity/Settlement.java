package com.fleetmanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the master booking
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private BigDecimal totalRevenue; // Total Amount paid by user

    @Column(nullable = false)
    private BigDecimal driverShare; // 70%

    @Column(nullable = false)
    private BigDecimal companyProfit; // 20%

    @Column(nullable = false)
    private BigDecimal maintenanceShare; // 10%

    @Column(nullable = false)
    private LocalDateTime settledAt;

    @PrePersist
    protected void onSettlement() {
        this.settledAt = LocalDateTime.now();
    }
}