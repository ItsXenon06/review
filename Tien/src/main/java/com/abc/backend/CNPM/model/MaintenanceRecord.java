package com.abc.backend.CNPM.model;

import com.abc.backend.CNPM.model.enums.MaintenanceType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "MaintenanceRecord")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RecordID")
    private Integer recordId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "VehicleID", nullable = false)
    private Vehicle vehicle;

    // Uses custom AttributeConverter (MaintenanceTypeConverter) for "Oil Change" ↔ OilChange
    @Column(name = "MaintenanceType", nullable = false, length = 50)
    private MaintenanceType maintenanceType;

    @Column(name = "Description", length = 500)
    private String description;

    @Column(name = "MileageAtService", nullable = false)
    private Integer mileageAtService;

    @Column(name = "ServiceDate", nullable = false)
    private LocalDate serviceDate;

    @Column(name = "NextServiceDate")
    private LocalDate nextServiceDate;

    @Column(name = "Cost", precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "ServiceProvider", length = 100)
    private String serviceProvider;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }
}

