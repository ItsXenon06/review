package com.abc.backend.CNPM.model;

import com.abc.backend.CNPM.model.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "InsuranceClaim")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InsuranceClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClaimID")
    private Integer claimId;

    /** One claim per damage (UNIQUE on DamageID) */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DamageID", nullable = false, unique = true)
    private DamageReport damageReport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "InsurancePlanID", nullable = false)
    private InsurancePlan insurancePlan;

    @Column(name = "ClaimDate", nullable = false)
    private LocalDate claimDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ClaimStatus", nullable = false, length = 20)
    private ClaimStatus claimStatus = ClaimStatus.Open;

    @Column(name = "ClaimAmount", nullable = false, precision = 10, scale = 2)
    private BigDecimal claimAmount;

    @Column(name = "DeductibleCharged", nullable = false, precision = 10, scale = 2)
    private BigDecimal deductibleCharged = BigDecimal.ZERO;

    @Column(name = "SettledAmount", precision = 10, scale = 2)
    private BigDecimal settledAmount;

    @Column(name = "SettledAt")
    private Instant settledAt;

    @Column(name = "Notes", length = 500)
    private String notes;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        if (claimDate == null) claimDate = LocalDate.now();
    }
}

