package com.abc.backend.CNPM.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PenaltyResultDTO {

    private String vehicleDisplayName;
    private String licensePlate;

    private long lateHours;
    private long lateDays;

    // Late return fee: 50% of next day rate per started day
    private BigDecimal lateReturnFee;

    // Over-km fee
    private int overKm;
    private BigDecimal overKmFee;

    private BigDecimal totalPenalty;

    private String breakdownNote;
}