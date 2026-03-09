package com.fleetmanagement.backend.repository;

import com.fleetmanagement.backend.entity.CompanyBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface CompanyBankRepository extends JpaRepository<CompanyBank, Long> {
    
    @Modifying
    @Query("UPDATE CompanyBank c SET " +
           "c.totalCompanyProfit = c.totalCompanyProfit + :profit, " +
           "c.totalMaintenanceFund = c.totalMaintenanceFund + :maintenance, " +
           "c.lifetimeRevenue = c.lifetimeRevenue + :revenue, " +
           "c.lastUpdatedAt = CURRENT_TIMESTAMP " +
           "WHERE c.id = 1")
    void incrementBankBalances(@Param("profit") BigDecimal profit, 
                               @Param("maintenance") BigDecimal maintenance,
                               @Param("revenue") BigDecimal revenue);
}