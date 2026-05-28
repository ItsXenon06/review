package com.abc.backend.CNPM.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ReservationPricingRule")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationPricingRule {

    @EmbeddedId
    private ReservationPricingRuleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("reservationId")
    @JoinColumn(name = "ReservationID")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ruleId")
    @JoinColumn(name = "RuleID")
    private PricingRule pricingRule;

    @Column(name = "AppliedMultiplier", nullable = false, precision = 5, scale = 2)
    private BigDecimal appliedMultiplier;
}


