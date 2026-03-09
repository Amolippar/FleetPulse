package com.fleetmanagement.backend.dto;

import java.math.BigDecimal;

public record CompanyFinanceAnalysisDTO(
    BigDecimal totalProfitInBank,
    BigDecimal availableMaintenanceBudget,
    BigDecimal totalBusinessTurnover,
    long totalCompletedTrips // Calculated from Booking table
) {}