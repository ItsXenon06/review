package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.InsuranceClaim;
import com.abc.backend.CNPM.model.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, Integer> {

    Optional<InsuranceClaim> findByDamageReportDamageId(Integer damageId);

    List<InsuranceClaim> findByClaimStatus(ClaimStatus claimStatus);
}
