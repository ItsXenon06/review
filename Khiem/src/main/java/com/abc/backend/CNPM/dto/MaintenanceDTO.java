package com.abc.backend.CNPM.dto;

import java.time.LocalDate;

public class MaintenanceDTO {
    private Integer vehicleId;
    private String make; // Đổi thành chữ thường
    private String licensePlate;
    private String description;
    private LocalDate nextServiceDate;

    // Constructor (đã khớp 5 tham số)
    public MaintenanceDTO(Integer vehicleId, String make, String licensePlate, String description, LocalDate nextServiceDate) {
        this.vehicleId = vehicleId;
        this.make = make;
        this.licensePlate = licensePlate;
        this.description = description;
        this.nextServiceDate = nextServiceDate;
    }

    // Getters
    public Integer getVehicleId() { return vehicleId; }
    public String getMake() { return make; } // Getter khớp với biến
    public String getLicensePlate() { return licensePlate; }
    public String getDescription() { return description; }
    public LocalDate getNextServiceDate() { return nextServiceDate; }
}