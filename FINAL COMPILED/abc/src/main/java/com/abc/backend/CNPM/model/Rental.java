package com.abc.backend.CNPM.model;

import com.abc.backend.CNPM.model.enums.FuelLevel;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "Rental")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RentalID")
    private Integer rentalId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ReservationID", nullable = false, unique = true)
    private Reservation reservation;

    @Column(name = "ActualPickupDate", nullable = false)
    private Instant actualPickupDate;

    @Column(name = "ActualReturnDate")
    private Instant actualReturnDate;

    @Column(name = "MileageOut", nullable = false)
    private Integer mileageOut;

    @Column(name = "MileageIn")
    private Integer mileageIn;

    // FuelLevelConverter handles "3/4","1/2","1/4" ↔ enum
    @Column(name = "FuelLevelOut", nullable = false, length = 10)
    private FuelLevel fuelLevelOut = FuelLevel.Full;

    @Column(name = "FuelLevelIn", length = 10)
    private FuelLevel fuelLevelIn;

    @Column(name = "BaseRentalCharge", precision = 10, scale = 2)
    private BigDecimal baseRentalCharge;

    @Column(name = "InsuranceCharge", precision = 10, scale = 2)
    private BigDecimal insuranceCharge;

    @Column(name = "ExtraCharges", nullable = false, precision = 10, scale = 2)
    private BigDecimal extraCharges = BigDecimal.ZERO;

    @Column(name = "ExtraChargesNote", length = 255)
    private String extraChargesNote;

    @Column(name = "TotalCharge", precision = 10, scale = 2)
    private BigDecimal totalCharge;

    @Column(name = "StaffPickupNotes", length = 500)
    private String staffPickupNotes;

    @Column(name = "StaffReturnNotes", length = 500)
    private String staffReturnNotes;

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

