package com.abc.backend.CNPM.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepreciationRequestDTO {

    @NotNull(message = "Vui lòng chọn xe")
    private Integer vehicleId;

    @NotNull(message = "Vui lòng nhập giá trị ban đầu")
    @Min(value = 1)
    private BigDecimal originalValue;

    @Min(value = 0)
    private int currentMileage = 0;

    /** Straight-line depreciation rate per year (default 20%) */
    private BigDecimal annualDepreciationRate = new BigDecimal("0.20");

    @Min(value = 1)
    private int vehicleAgeYears = 1;
}