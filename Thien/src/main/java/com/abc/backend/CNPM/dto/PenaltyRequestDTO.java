package com.abc.backend.CNPM.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PenaltyRequestDTO {

    @NotNull(message = "Vui lòng chọn xe")
    private Integer vehicleId;

    @NotNull(message = "Vui lòng nhập ngày trả dự kiến")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate agreedReturnDate;

    private LocalTime agreedReturnTime = LocalTime.of(8, 0);

    @NotNull(message = "Vui lòng nhập ngày trả thực tế")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualReturnDate;

    private LocalTime actualReturnTime = LocalTime.of(8, 0);

    @NotNull(message = "Vui lòng nhập giá thuê theo ngày")
    @Min(value = 0)
    private BigDecimal dailyRate;

    @Min(value = 0)
    private int actualKm = 0;

    @Min(value = 0)
    private int includedKm = 0;
}