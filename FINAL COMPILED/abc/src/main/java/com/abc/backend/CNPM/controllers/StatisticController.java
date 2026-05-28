package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.service.StatisticService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Autowired
private VehicleRepository vehicleRepository;
@Controller
public class StatisticController {

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/statistic")
    public String viewDashboard(Model model) {
        long totalVehicles    = vehicleRepository.count();
long rentedVehicles   = vehicleRepository.countByStatus(VehicleStatus.Rented);
String utilizationRate = totalVehicles > 0
    ? String.format("%.1f", (rentedVehicles * 100.0 / totalVehicles))
    : "0";

model.addAttribute("totalVehicles",   totalVehicles);
model.addAttribute("utilizationRate", utilizationRate);
        model.addAttribute("totalRevenue", statisticService.getTotalRevenue());
        model.addAttribute("totalBookings", statisticService.getTotalBookings());
        model.addAttribute("vehicleStats", statisticService.getVehicleStatusStats());
        model.addAttribute("monthlyRevenue", statisticService.getMonthlyRevenueStats());
        return "statistic";
    }
}