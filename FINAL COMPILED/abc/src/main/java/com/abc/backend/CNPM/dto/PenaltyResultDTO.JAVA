package com.abc.backend.CNPM.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PenaltyResultDTO {
    private String     vehicleDisplayName;
    private String     licensePlate;
    private long       lateHours;
    private long       lateDays;
    private BigDecimal lateReturnFee;
    private int        overKm;
    private BigDecimal overKmFee;
    private BigDecimal totalPenalty;
    private String     breakdownNote;
}