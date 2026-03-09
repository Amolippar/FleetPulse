package com.fleetmanagement.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SettlementReportDTO(
    Long settlementId,
    String bookingNumber,
    String driverName,
    BigDecimal totalRevenue,
    BigDecimal driverCut,
    BigDecimal maintenanceFund,
    BigDecimal netProfit,
    LocalDateTime date
) {}