package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.dto.MaintenanceDTO;
import com.abc.backend.CNPM.service.ContractService;
import com.abc.backend.CNPM.service.MaintenanceService;
import com.abc.backend.CNPM.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private MaintenanceService maintenanceService; // Inject service mới vào

    @GetMapping
    public String showDashboard(Model model) {
        // 1. Xử lý logic hiển thị Hợp đồng (như cũ)
        List<Contract> rawList = contractService.findAllExpiring();
        List<Contract> cleanList = new ArrayList<>();

        for (Contract c : rawList) {
            if (c != null && c.getContractID() != null) {
                cleanList.add(c);
            }
        }
        model.addAttribute("expiringContracts", cleanList);

        // 2. Xử lý logic hiển thị Bảo dưỡng (đã sửa lỗi trùng lặp)
        List<MaintenanceDTO> maintenanceList = maintenanceService.findUpcomingMaintenance();
        model.addAttribute("upcomingMaintenances", maintenanceList);

        return "main-dashboard";
    }
}