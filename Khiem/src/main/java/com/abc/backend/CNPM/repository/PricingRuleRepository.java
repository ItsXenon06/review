package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.PricingRule;
import com.abc.backend.CNPM.model.enums.PricingRuleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PricingRuleRepository extends JpaRepository<PricingRule, Integer> {

    List<PricingRule> findByIsActiveTrue();

    List<PricingRule> findByRuleTypeAndIsActiveTrue(PricingRuleType ruleType);

    /** Active rules that cover a given date (or have no date range). */
    List<PricingRule> findByIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate start, LocalDate end);
}
