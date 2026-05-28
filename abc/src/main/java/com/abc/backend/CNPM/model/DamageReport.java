package com.abc.backend.CNPM.model;

import com.abc.backend.CNPM.model.enums.DamageType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "DamageReport")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DamageReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DamageID")
    private Integer damageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RentalID", nullable = false)
    private Rental rental;

    @Column(name = "ReportedAt", nullable = false)
    private Instant reportedAt;

    // DamageTypeConverter handles "Total Loss" ↔ TotalLoss
    @Column(name = "DamageType", nullable = false, length = 50)
    private DamageType damageType;

    @Column(name = "Location", length = 100)
    private String location;

    @Column(name = "Description", length = 1000)
    private String description;

    @Column(name = "EstimatedRepairCost", precision = 10, scale = 2)
    private BigDecimal estimatedRepairCost;

    @Column(name = "ActualRepairCost", precision = 10, scale = 2)
    private BigDecimal actualRepairCost;

    @Column(name = "CustomerLiable", nullable = false)
    private Boolean customerLiable = true;

    @Column(name = "InsuranceClaimed", nullable = false)
    private Boolean insuranceClaimed = false;

    @PrePersist
    protected void onCreate() {
        if (reportedAt == null) reportedAt = Instant.now();
    }
}

