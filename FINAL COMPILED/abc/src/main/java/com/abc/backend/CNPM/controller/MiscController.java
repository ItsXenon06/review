package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.service.ContractService;
import com.abc.backend.CNPM.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MiscController {

    private final ContractService contractService;
    private final MaintenanceService maintenanceService;

    @GetMapping("/notifications")
    public String notificationsPage(Model model) {
        model.addAttribute("expiringContracts", contractService.findAllExpiring());
        model.addAttribute("maintenanceList", maintenanceService.findUpcomingMaintenance());
        return "main-dashboard";
    }

    @GetMapping("/settings")
    public String settingsPage() {
        return "settings";
    }

    @GetMapping("/customers")
    public String customersPage() {
        return "redirect:/storage?tab=customer";
    }

    @GetMapping("/vehicles")
    public String vehiclesPage() {
        return "redirect:/storage?tab=vehicle";
    }
    @GetMapping("/")
public String rootRedirect() {
    return "redirect:/dashboard";
}
}