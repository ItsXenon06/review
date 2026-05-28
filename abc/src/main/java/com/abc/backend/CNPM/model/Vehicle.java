package com.abc.backend.CNPM.model;

import com.abc.backend.CNPM.model.enums.FuelType;
import com.abc.backend.CNPM.model.enums.TransmissionType;
import com.abc.backend.CNPM.model.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "Vehicle")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VehicleID")
    private Integer vehicleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CategoryID", nullable = false)
    private VehicleCategory category;

    @Column(name = "LicensePlate", nullable = false, unique = true, length = 20)
    private String licensePlate;

    @Column(name = "Make", nullable = false, length = 50)
    private String make;

    @Column(name = "Model", nullable = false, length = 50)
    private String model;

    @Column(name = "Year", nullable = false)
    private Short year;

    @Column(name = "Color", length = 30)
    private String color;

    @Column(name = "VIN", nullable = false, unique = true, length = 17)
    private String vin;

    @Enumerated(EnumType.STRING)
    @Column(name = "FuelType", nullable = false, length = 20)
    private FuelType fuelType = FuelType.Gasoline;

    @Enumerated(EnumType.STRING)
    @Column(name = "TransmissionType", nullable = false, length = 10)
    private TransmissionType transmissionType = TransmissionType.Automatic;

    @Column(name = "SeatingCapacity", nullable = false)
    private Short seatingCapacity = 5;

    @Column(name = "CurrentMileage", nullable = false)
    private Integer currentMileage = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 20)
    private VehicleStatus status = VehicleStatus.Available;

    @Column(name = "LastServiceDate")
    private LocalDate lastServiceDate;

    @Column(name = "Notes", length = 500)
    private String notes;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}

