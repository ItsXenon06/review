package com.abc.backend.CNPM.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "InsurancePlan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PlanID")
    private Integer planId;

    @Column(name = "PlanName", nullable = false, length = 100)
    private String planName;

    @Column(name = "DailyPremium", nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyPremium;

    @Column(name = "DeductibleAmt", nullable = false, precision = 10, scale = 2)
    private BigDecimal deductibleAmt;

    @Column(name = "CoverageDetails", columnDefinition = "TEXT")
    private String coverageDetails;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = true;
}

