package com.abc.backend.CNPM.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CalculationRequestDTO {
    @NotNull(message = "Vui lòng chọn xe")
    private Integer   vehicleId;
    @NotNull(message = "Vui lòng chọn ngày bắt đầu")
    private LocalDate startDate;
    private LocalTime startTime;
    @NotNull(message = "Vui lòng chọn ngày kết thúc")
    private LocalDate endDate;
    private LocalTime endTime;
    private Integer   estimatedKm;
    private String    rentalType    = "SELF_DRIVE";
    private Integer   insurancePlanId;
    private BigDecimal deposit      = BigDecimal.ZERO;
}
