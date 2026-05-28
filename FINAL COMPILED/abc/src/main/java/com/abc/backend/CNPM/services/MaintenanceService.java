package com.abc.backend.CNPM.Service;

import com.abc.backend.CNPM.dto.MaintenanceDTO;
import com.abc.backend.CNPM.repository.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    public List<MaintenanceDTO> findUpcomingMaintenance() {
        // Truyền ngày hiện tại (LocalDate.now()) vào
        return maintenanceRecordRepository.findUpcomingMaintenance(LocalDate.now());
    }
}
