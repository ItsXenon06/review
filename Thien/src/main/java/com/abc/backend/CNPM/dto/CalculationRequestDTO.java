package com.abc.backend.CNPM.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CalculationRequestDTO {

    @NotNull(message = "Vui lòng chọn xe")
    private Integer vehicleId;

    @NotNull(message = "Vui lòng chọn ngày bắt đầu")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    private LocalTime startTime = LocalTime.of(8, 0);

    @NotNull(message = "Vui lòng chọn ngày kết thúc")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private LocalTime endTime = LocalTime.of(8, 0);

    @Min(value = 0, message = "Số km không hợp lệ")
    private Integer estimatedKm = 0;

    /** 'SELF_DRIVE' or 'WITH_DRIVER' */
    private String rentalType = "SELF_DRIVE";

    private Integer insurancePlanId;

    @Min(value = 0, message = "Đặt cọc không hợp lệ")
    private BigDecimal deposit = BigDecimal.ZERO;

    // Populated server-side for display purposes
    private String vehicleDisplayName;
    private String licensePlate;
    private String vehicleType;
    private BigDecimal dailyRate;
}