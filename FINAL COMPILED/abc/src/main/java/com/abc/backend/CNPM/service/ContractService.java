package com.abc.backend.CNPM.service;

import com.abc.backend.CNPM.model.Contract;
import com.abc.backend.CNPM.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    // ONLY expiring within 7 days OR already expired
    public List<Contract> findAllExpiring() {
        LocalDateTime threshold = LocalDateTime.now().plusDays(7);

        return contractRepository.findAll().stream()
                .filter(c -> c.getEndDate() != null && c.getEndDate().isBefore(threshold))
                .sorted(Comparator.comparing(Contract::getEndDate))
                .collect(Collectors.toList());
    }

    // Check single contract expiry status
    public String checkAndSendNotification(Integer contractId) {
        Optional<Contract> contractOpt = contractRepository.findById(contractId);

        if (contractOpt.isEmpty()) {
            return "NOT_FOUND";
        }

        Contract contract = contractOpt.get();
        LocalDateTime now = LocalDateTime.now();

        if (contract.getEndDate() != null && contract.getEndDate().isBefore(now)) {
            return "SUCCESS";
        }

        return "NOT_EXPIRED";
    }
}