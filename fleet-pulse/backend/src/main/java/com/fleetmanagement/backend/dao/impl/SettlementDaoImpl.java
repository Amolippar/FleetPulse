package com.fleetmanagement.backend.dao.impl;

import com.fleetmanagement.backend.dao.SettlementDao;
import com.fleetmanagement.backend.entity.Settlement;
import com.fleetmanagement.backend.repository.SettlementRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SettlementDaoImpl implements SettlementDao {

    private final SettlementRepository repository;

    public SettlementDaoImpl(SettlementRepository repository) {
        this.repository = repository;
    }

    @Override
    public Settlement save(Settlement settlement) {
        return repository.save(settlement);
    }

    @Override
    public List<Settlement> findAll() {
        return repository.findAll();
    }
}