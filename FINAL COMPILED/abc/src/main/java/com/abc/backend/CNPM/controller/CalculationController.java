package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.dto.*;
import com.abc.backend.CNPM.model.Vehicle;
import com.abc.backend.CNPM.repository.InsurancePlanRepository;
import com.abc.backend.CNPM.repository.VehicleRepository;
import com.abc.backend.CNPM.service.CalculationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.abc.backend.CNPM.dto.PenaltyResultDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/calculation")
@RequiredArgsConstructor
public class CalculationController {

    private final CalculationService    calculationService;
    private final VehicleRepository     vehicleRepository;
    private final InsurancePlanRepository insurancePlanRepository;

    /** GET /calculation — show empty form */
    @GetMapping
    public String showCalculation(Model model) {
        model.addAttribute("calcRequest",        new CalculationRequestDTO());
        model.addAttribute("penaltyRequest",     new PenaltyRequestDTO());
        model.addAttribute("depreciationRequest",new DepreciationRequestDTO());
        model.addAttribute("vehicles",           vehicleRepository.findAll());
        model.addAttribute("insurancePlans",     insurancePlanRepository.findByIsActiveTrue());
        model.addAttribute("activeTab",          "rental");
        return "index";
    }

    /** POST /calculation/rental — compute rental cost */
    @PostMapping("/rental")
    public String calculateRental(
            @Valid @ModelAttribute("calcRequest") CalculationRequestDTO req,
            BindingResult bindingResult,
            Model model) {

        model.addAttribute("penaltyRequest",     new PenaltyRequestDTO());
        model.addAttribute("depreciationRequest",new DepreciationRequestDTO());
        model.addAttribute("vehicles",           vehicleRepository.findAll());
        model.addAttribute("insurancePlans",     insurancePlanRepository.findByIsActiveTrue());
        model.addAttribute("activeTab",          "rental");

        if (bindingResult.hasErrors()) {
            return "index";
        }
        try {
            CalculationResultDTO result = calculationService.calculateRentalCost(req);
            model.addAttribute("calcResult", result);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "index";
    }

    /** POST /calculation/penalty — compute late/over-km penalty */
    @PostMapping("/penalty")
    public String calculatePenalty(
            @Valid @ModelAttribute("penaltyRequest") PenaltyRequestDTO req,
            BindingResult bindingResult,
            Model model) {

        model.addAttribute("calcRequest",        new CalculationRequestDTO());
        model.addAttribute("depreciationRequest",new DepreciationRequestDTO());
        model.addAttribute("vehicles",           vehicleRepository.findAll());
        model.addAttribute("insurancePlans",     insurancePlanRepository.findByIsActiveTrue());
        model.addAttribute("activeTab",          "penalty");

        if (bindingResult.hasErrors()) {
            return "index";
        }
        try {
            PenaltyResultDTO result = calculationService.calculatePenalty(req);
            model.addAttribute("penaltyResult", result);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "index";
    }

    /** POST /calculation/depreciation — compute vehicle depreciation */
    @PostMapping("/depreciation")
    public String calculateDepreciation(
            @Valid @ModelAttribute("depreciationRequest") DepreciationRequestDTO req,
            BindingResult bindingResult,
            Model model) {

        model.addAttribute("calcRequest",    new CalculationRequestDTO());
        model.addAttribute("penaltyRequest", new PenaltyRequestDTO());
        model.addAttribute("vehicles",       vehicleRepository.findAll());
        model.addAttribute("insurancePlans", insurancePlanRepository.findByIsActiveTrue());
        model.addAttribute("activeTab",      "depreciation");

        if (bindingResult.hasErrors()) {
            return "index";
        }
        try {
            DepreciationResultDTO result = calculationService.calculateDepreciation(req);
            model.addAttribute("depreciationResult", result);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "index";
    }

    /** GET /calculation/vehicle-info/{id} — AJAX endpoint for vehicle details */
    @GetMapping("/vehicle-info/{id}")
    @ResponseBody
    public Map<String, Object> getVehicleInfo(@PathVariable Integer id) {
        return vehicleRepository.findById(id)
                .map(v -> Map.<String, Object>of(
                        "displayName",  v.getMake() + " " + v.getModel() + " " + v.getYear(),
                        "licensePlate", v.getLicensePlate(),
                        "dailyRate",    v.getCategory().getBaseDailyRate(),
                        "seatInfo",     v.getSeatingCapacity() + " chỗ"
                ))
                .orElse(Map.of());
    }
}
