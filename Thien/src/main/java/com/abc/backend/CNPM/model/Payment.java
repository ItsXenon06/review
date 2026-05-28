package com.abc.backend.CNPM.model;

import com.abc.backend.CNPM.model.enums.PaymentMethod;
import com.abc.backend.CNPM.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "Payment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentID")
    private Integer paymentId;

    /**
     * Exactly one of reservationId / rentalId must be non-null.
     * The DB enforces this via CHK_Payment_OneParent.
     * In service layer, validate before saving.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReservationID")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RentalID")
    private Rental rental;

    @Column(name = "PaymentDate", nullable = false)
    private Instant paymentDate;

    @Column(name = "Amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "PaymentMethod", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "ReferenceNo", length = 100)
    private String referenceNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "PaymentType", nullable = false, length = 20)
    private PaymentType paymentType = PaymentType.Charge;

    @Column(name = "Notes", length = 255)
    private String notes;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        if (paymentDate == null) paymentDate = Instant.now();
    }
}

