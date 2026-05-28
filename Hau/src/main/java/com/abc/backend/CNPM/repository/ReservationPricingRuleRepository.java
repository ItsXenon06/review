package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.ReservationPricingRule;
import com.abc.backend.CNPM.model.ReservationPricingRuleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationPricingRuleRepository
        extends JpaRepository<ReservationPricingRule, ReservationPricingRuleId> {

    List<ReservationPricingRule> findByIdReservationId(Integer reservationId);
}

