package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.dto.*;
import com.abc.backend.CNPM.model.InsurancePlan;
import com.abc.backend.CNPM.model.Vehicle;
import com.abc.backend.CNPM.model.enums.VehicleStatus;
import com.abc.backend.CNPM.repository.InsurancePlanRepository;
import com.abc.backend.CNPM.repository.VehicleRepository;
import com.abc.backend.CNPM.service.CalculationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/calculation")
public class CalculationController {

    @Autowired private CalculationService calculationService;
    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private InsurancePlanRepository insurancePlanRepository;

    private void addCommonAttributes(Model model) {
        List<Vehicle> vehicles = vehicleRepository.findByStatusNot(VehicleStatus.Retired);
        List<InsurancePlan> plans = insurancePlanRepository.findByIsActiveTrue();
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("insurancePlans", plans);
    }

    @GetMapping
    public String showCalculationPage(Model model) {
        addCommonAttributes(model);
        model.addAttribute("calcRequest", new CalculationRequestDTO());
        model.addAttribute("penaltyRequest", new PenaltyRequestDTO());
        model.addAttribute("depreciationRequest", new DepreciationRequestDTO());
        model.addAttribute("activeTab", "rental");
        return "calculation/index";
    }

    @PostMapping("/rental")
    public String calculateRental(
            @Valid @ModelAttribute("calcRequest") CalculationRequestDTO request,
            BindingResult bindingResult,
            Model model) {

        addCommonAttributes(model);
        model.addAttribute("activeTab", "rental");

        // ĐỒNG BỘ: Thêm 2 dòng này để tránh sập các Tab còn lại khi render
        model.addAttribute("penaltyRequest", new PenaltyRequestDTO());
        model.addAttribute("depreciationRequest", new DepreciationRequestDTO());

        if (!bindingResult.hasErrors()) {
            try {
                model.addAttribute("calcResult", calculationService.calculateRentalCost(request));
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        }
        return "calculation/index";
    }

    @PostMapping("/penalty")
    public String calculatePenalty(
            @Valid @ModelAttribute("penaltyRequest") PenaltyRequestDTO request,
            BindingResult bindingResult,
            Model model) {

        addCommonAttributes(model);
        model.addAttribute("calcRequest", new CalculationRequestDTO());
        model.addAttribute("depreciationRequest", new DepreciationRequestDTO());
        model.addAttribute("activeTab", "penalty");

        if (!bindingResult.hasErrors()) {
            try {
                model.addAttribute("penaltyResult", calculationService.calculatePenalty(request));
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        }
        return "calculation/index";
    }

    @PostMapping("/depreciation")
    public String calculateDepreciation(
            @Valid @ModelAttribute("depreciationRequest") DepreciationRequestDTO request,
            BindingResult bindingResult,
            Model model) {

        addCommonAttributes(model);
        model.addAttribute("calcRequest", new CalculationRequestDTO());
        model.addAttribute("penaltyRequest", new PenaltyRequestDTO());
        model.addAttribute("activeTab", "depreciation");

        if (!bindingResult.hasErrors()) {
            try {
                model.addAttribute("depreciationResult", calculationService.calculateDepreciation(request));
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        }
        return "calculation/index";
    }

    @GetMapping("/vehicle-info/{id}")
    @ResponseBody
    public VehicleInfoResponse getVehicleInfo(@PathVariable Integer id) {
        return vehicleRepository.findById(id)
                .map(v -> new VehicleInfoResponse(
                        v.getMake() + " " + v.getModel() + " " + v.getYear(),
                        v.getLicensePlate(),
                        v.getSeatingCapacity() + " chỗ",
                        v.getCategory().getBaseDailyRate()
                ))
                .orElse(null);
    }

    public record VehicleInfoResponse(
            String displayName,
            String licensePlate,
            String seatInfo,
            java.math.BigDecimal dailyRate
    ) {}
}