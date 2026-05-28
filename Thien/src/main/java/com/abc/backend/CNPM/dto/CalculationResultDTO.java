package com.abc.backend.CNPM.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CalculationResultDTO {

    // Vehicle summary
    private String vehicleDisplayName;
    private String licensePlate;
    private String vehicleType;
    private BigDecimal dailyRate;

    // Rental period
    private int totalDays;
    private int extraHours;
    private String rentalType;

    // Km
    private int estimatedKm;
    private int includedKmPerDay;      // 200 km/day default
    private int totalIncludedKm;
    private int overKm;
    private BigDecimal overKmFeePerKm; // 2,000 VND default
    private BigDecimal overKmCharge;

    // Cost breakdown
    private BigDecimal baseRentalCharge;   // dailyRate * days
    private BigDecimal insuranceDailyRate;
    private BigDecimal insuranceCharge;    // insuranceRate * days
    private BigDecimal cleaningFee;        // fixed 100,000
    private BigDecimal subtotal;           // before VAT
    private BigDecimal vatRate;            // 0.08
    private BigDecimal vatAmount;
    private BigDecimal totalCharge;        // subtotal + VAT

    // Deposit summary
    private BigDecimal deposit;
    private BigDecimal remainingAmount;    // totalCharge - deposit

    // Policy notes
    private String cancellationPolicy;
}