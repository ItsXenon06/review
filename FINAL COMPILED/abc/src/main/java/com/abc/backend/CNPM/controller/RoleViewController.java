package com.abc.backend.CNPM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoleViewController {

    @GetMapping("/roles")
    public String rolesPage() {
        return "THOA";
    }
}