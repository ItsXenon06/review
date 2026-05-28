package com.abc.backend.CNPM.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PenaltyRequestDTO {
    @NotNull(message = "Vui lòng chọn xe")
    private Integer    vehicleId;
    @NotNull(message = "Vui lòng nhập giá thuê")
    private BigDecimal dailyRate;
    @NotNull(message = "Vui lòng nhập ngày trả dự kiến")
    private LocalDate  agreedReturnDate;
    private LocalTime  agreedReturnTime;
    @NotNull(message = "Vui lòng nhập ngày trả thực tế")
    private LocalDate  actualReturnDate;
    private LocalTime  actualReturnTime;
    private int        actualKm   = 0;
    private int        includedKm = 0;
}
