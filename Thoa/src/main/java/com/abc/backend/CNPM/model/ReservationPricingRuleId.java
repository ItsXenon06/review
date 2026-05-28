package com.abc.backend.CNPM.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReservationPricingRuleId implements Serializable {

    @Column(name = "ReservationID")
    private Integer reservationId;

    @Column(name = "RuleID")
    private Integer ruleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationPricingRuleId that)) return false;
        return Objects.equals(reservationId, that.reservationId) &&
                Objects.equals(ruleId, that.ruleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, ruleId);
    }
}
