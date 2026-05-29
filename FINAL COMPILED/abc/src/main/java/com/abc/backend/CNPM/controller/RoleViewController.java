package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.repository.PhanQuyen.VaiTroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RoleViewController {

    private final VaiTroRepository vaiTroRepository;

    @GetMapping("/roles")
    public String rolesPage(Model model) {
        model.addAttribute("vaiTros", vaiTroRepository.findAll());
        return "THOA";
    }
}
