package com.abc.backend.CNPM.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DepreciationResultDTO {

    private String vehicleDisplayName;
    private String licensePlate;
    private int vehicleYear;

    private BigDecimal originalValue;
    private BigDecimal totalDepreciationAmount;
    private BigDecimal currentEstimatedValue;
    private BigDecimal depreciationPercent;

    private int vehicleAgeYears;
    private BigDecimal annualDepreciationRate;
    private BigDecimal annualDepreciationAmount;

    // Year-by-year breakdown
    private List<YearRow> yearlyBreakdown;

    @Data
    @Builder
    public static class YearRow {
        private int year;
        private BigDecimal depreciationAmount;
        private BigDecimal bookValue;
    }
}