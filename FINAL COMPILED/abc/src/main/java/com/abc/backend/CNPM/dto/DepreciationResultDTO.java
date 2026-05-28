package com.abc.backend.CNPM.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DepreciationResultDTO {
    private String     vehicleDisplayName;
    private String     licensePlate;
    private int        vehicleYear;
    private BigDecimal originalValue;
    private BigDecimal totalDepreciationAmount;
    private BigDecimal currentEstimatedValue;
    private BigDecimal depreciationPercent;
    private int        vehicleAgeYears;
    private BigDecimal annualDepreciationRate;
    private BigDecimal annualDepreciationAmount;
    private List<YearRow> yearlyBreakdown;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class YearRow {
        private int        year;
        private BigDecimal depreciationAmount;
        private BigDecimal bookValue;
    }
}
