package com.fleetmanagement.backend.config;


import com.fleetmanagement.backend.dao.CompanyBankDao;
import com.fleetmanagement.backend.entity.CompanyBank;
import com.fleetmanagement.backend.repository.CompanyBankRepository;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final CompanyBankDao companyBankDao;
    private final CompanyBankRepository repository; // Add the repository

    public DataInitializer(CompanyBankDao companyBankDao, CompanyBankRepository repository) {
        this.companyBankDao = companyBankDao;
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        // Check if ANY record exists
        if (repository.count() == 0) {
            CompanyBank master = CompanyBank.builder()
                    .totalCompanyProfit(BigDecimal.ZERO)
                    .totalMaintenanceFund(BigDecimal.ZERO)
                    .lifetimeRevenue(BigDecimal.ZERO)
                    .build();
            
            repository.save(master);
            System.out.println("★ Schema was empty. Created initial CompanyBank record.");
        } else {
            System.out.println("✓ CompanyBank record already exists.");
        }
    }
}