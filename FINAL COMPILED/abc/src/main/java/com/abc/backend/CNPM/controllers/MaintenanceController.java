package com.abc.backend.CNPM.Controller;
import com.abc.backend.CNPM.model.MaintenanceRecord;
import com.abc.backend.CNPM.dto.MaintenanceDTO;
import com.abc.backend.CNPM.Service.MaintenanceService; // Đảm bảo đã import
import com.abc.backend.CNPM.repository.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.time.LocalDate;
@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping("/list")
    public List<MaintenanceDTO> getMaintenanceList() {
        return maintenanceService.findUpcomingMaintenance();
    }
}