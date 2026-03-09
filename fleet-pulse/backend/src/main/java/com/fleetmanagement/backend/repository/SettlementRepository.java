package com.fleetmanagement.backend.repository;

import com.fleetmanagement.backend.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    // Basic JpaRepository methods handle saving
}