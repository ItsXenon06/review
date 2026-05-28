package com.abc.backend.CNPM.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DepreciationRequestDTO {
    @NotNull(message = "Vui lòng chọn xe")
    private Integer    vehicleId;
    @NotNull(message = "Vui lòng nhập giá trị ban đầu")
    @Min(value = 1, message = "Giá trị phải lớn hơn 0")
    private BigDecimal originalValue;
    private BigDecimal annualDepreciationRate = new BigDecimal("20");
    private int        vehicleAgeYears        = 3;
    private int        currentMileage         = 0;
}
