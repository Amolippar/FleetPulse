package com.fleetmanagement.backend.dao;

import com.fleetmanagement.backend.entity.CompanyBank;
import java.math.BigDecimal;

public interface CompanyBankDao {
    /**
     * Retrieves the single master record for company finances.
     * If it doesn't exist (first time use), it initializes one.
     */
    CompanyBank getMasterBank();

    /**
     * Persists the updated financial totals.
     */
    void save(CompanyBank bank);
    
    /**
     * Atomically increment bank balances to avoid concurrency issues.
     */
    void incrementBankBalances(BigDecimal profit, BigDecimal maintenance, BigDecimal revenue);
}