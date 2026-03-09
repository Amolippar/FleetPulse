package com.fleetmanagement.backend.dao;

import com.fleetmanagement.backend.entity.Settlement;
import java.util.List;

public interface SettlementDao {
    Settlement save(Settlement settlement);
    List<Settlement> findAll();
}