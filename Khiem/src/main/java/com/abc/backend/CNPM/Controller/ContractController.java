package com.abc.backend.CNPM.Controller;

import com.abc.backend.CNPM.Service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dashboard")
public class ContractController {

    @Autowired
    private ContractService contractService;

    // Chỉ giữ lại logic hiển thị View
    @GetMapping("/notifications")
    public String showNotifications(Model model) {
        model.addAttribute("expiringContracts", contractService.findAllExpiring());
        return "main-dashboard";
    }

    // ĐÃ XÓA phương thức @PostMapping trùng lặp ở đây
}