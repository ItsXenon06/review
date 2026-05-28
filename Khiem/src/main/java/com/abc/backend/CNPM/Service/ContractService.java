package com.abc.backend.CNPM.Service;

import com.abc.backend.CNPM.model.Contract;
import com.abc.backend.CNPM.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; // Đảm bảo đã import cái này
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    public List<Contract> findAllExpiring() {
        return contractRepository.findAll();
    }

    // Phương thức kiểm tra và gửi thông báo
    public String checkAndSendNotification(Integer contractId) {
        // Bây giờ findById sẽ nhận vào Integer và hoạt động bình thường
        Optional<Contract> contractOpt = contractRepository.findById(contractId);

        if (contractOpt.isEmpty()) {
            return "NOT_FOUND";
        }

        Contract contract = contractOpt.get();
        LocalDateTime now = LocalDateTime.now();

        if (contract.getEndDate() != null && contract.getEndDate().isBefore(now)) {
            return "SUCCESS";
        } else {
            return "NOT_EXPIRED";
        }
    }
}