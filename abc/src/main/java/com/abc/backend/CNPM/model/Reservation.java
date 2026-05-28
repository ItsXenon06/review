package com.abc.backend.CNPM.model;

import com.abc.backend.CNPM.model.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "Reservation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReservationID")
    private Integer reservationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CustomerID", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "VehicleID", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "InsurancePlanID")
    private InsurancePlan insurancePlan;

    @Column(name = "PickupDate", nullable = false)
    private LocalDate pickupDate;

    @Column(name = "ReturnDate", nullable = false)
    private LocalDate returnDate;

    /**
     * Mirrors the DB computed column DATEDIFF(DAY, PickupDate, ReturnDate).
     * Not stored; derived on read. Use @Transient to keep it out of DML.
     */
    @Transient
    public long getTotalDays() {
        if (pickupDate == null || returnDate == null) return 0;
        return ChronoUnit.DAYS.between(pickupDate, returnDate);
    }

    @Column(name = "QuotedDailyRate", nullable = false, precision = 10, scale = 2)
    private BigDecimal quotedDailyRate;

    @Column(name = "EstimatedTotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.Pending;

    @Column(name = "SpecialRequests", length = 500)
    private String specialRequests;

    @Column(name = "CancelledAt")
    private Instant cancelledAt;

    @Column(name = "CancelReason", length = 255)
    private String cancelReason;

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

