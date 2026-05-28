package com.abc.backend.CNPM.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceDTO {
    private Integer   vehicleId;
    private String    make;
    private String    licensePlate;
    private String    description;
    private LocalDate nextServiceDate;
}
