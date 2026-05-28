package com.abc.backend.CNPM.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CalculationResultDTO {
    private String     vehicleDisplayName;
    private String     licensePlate;
    private String     vehicleType;
    private BigDecimal dailyRate;
    private int        totalDays;
    private int        extraHours;
    private String     rentalType;
    private int        estimatedKm;
    private int        includedKmPerDay;
    private int        totalIncludedKm;
    private int        overKm;
    private BigDecimal overKmFeePerKm;
    private BigDecimal overKmCharge;
    private BigDecimal baseRentalCharge;
    private BigDecimal insuranceDailyRate;
    private BigDecimal insuranceCharge;
    private BigDecimal cleaningFee;
    private BigDecimal subtotal;
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalCharge;
    private BigDecimal deposit;
    private BigDecimal remainingAmount;
    private String     cancellationPolicy;
}