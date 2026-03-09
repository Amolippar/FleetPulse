package com.fleetmanagement.backend.dao.impl;

import com.fleetmanagement.backend.dao.CompanyBankDao;
import com.fleetmanagement.backend.entity.CompanyBank;
import com.fleetmanagement.backend.repository.CompanyBankRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class CompanyBankDaoImpl implements CompanyBankDao {

    private final CompanyBankRepository repository;

    public CompanyBankDaoImpl(CompanyBankRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompanyBank getMasterBank() {
        return repository.findById(1L).orElseGet(() -> {
            // Initialize if first time
            CompanyBank bank = CompanyBank.builder()
                    .id(1L)
                    .totalCompanyProfit(BigDecimal.ZERO)
                    .totalMaintenanceFund(BigDecimal.ZERO)
                    .lifetimeRevenue(BigDecimal.ZERO)
                    .lastUpdatedAt(LocalDateTime.now())
                    .build();
            return repository.save(bank);
        });
    }

    @Override
    public void save(CompanyBank bank) {
        repository.save(bank);
    }

    @Override
    @Transactional
    public void incrementBankBalances(BigDecimal profit, BigDecimal maintenance, BigDecimal revenue) {
        repository.incrementBankBalances(profit, maintenance, revenue);
    }
}