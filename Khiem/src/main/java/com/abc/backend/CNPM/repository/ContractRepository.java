package com.abc.backend.CNPM.repository;
import java.time.LocalDateTime;
import com.abc.backend.CNPM.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Thiếu import này
import org.springframework.stereotype.Repository;
import java.util.List; // Thiếu import này

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query("SELECT c FROM Contract c LEFT JOIN FETCH c.customer")
    List<Contract> findAllWithContractAndCustomer();
    List<Contract> findTop3ByEndDateAfterOrderByEndDateAsc(LocalDateTime now);
}