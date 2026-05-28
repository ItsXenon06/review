package com.abc.backend.CNPM.model;

import com.abc.backend.CNPM.model.enums.PricingRuleType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "PricingRule")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RuleID")
    private Integer ruleId;

    @Column(name = "RuleName", nullable = false, length = 100)
    private String ruleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "RuleType", nullable = false, length = 30)
    private PricingRuleType ruleType;

    /** e.g. 1.30 = +30 %, 0.85 = −15 % */
    @Column(name = "MultiplierPct", nullable = false, precision = 5, scale = 2)
    private BigDecimal multiplierPct;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    /** ISO day-of-week list e.g. "6,7" = Sat,Sun */
    @Column(name = "DaysOfWeek", length = 20)
    private String daysOfWeek;

    /** Fleet-occupancy % that triggers a DEMAND rule */
    @Column(name = "MinOccupancyPct", precision = 5, scale = 2)
    private BigDecimal minOccupancyPct;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = true;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        if (isActive == null) isActive = true;
    }
}
