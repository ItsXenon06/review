package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContractController {

    @Autowired
    private ContractService contractService;

    // Chỉ giữ lại logic hiển thị View
    @GetMapping("/contracts")
    public String contractsPage(Model model) {
        model.addAttribute("expiringContracts", contractService.findAllExpiring());
        return "main-dashboard";
    }
    
    // ĐÃ XÓA phương thức @PostMapping trùng lặp ở đây
}