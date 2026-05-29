package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.model.*;
import com.abc.backend.CNPM.model.enums.*;
import com.abc.backend.CNPM.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.util.List;


@Controller
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final VehicleRepository vehicleRepository;
    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final RentalRepository rentalRepository;
    private final InsurancePlanRepository insurancePlanRepository;

    // ── VEHICLES ──────────────────────────────────────────────

    @GetMapping
    public String storagePage(
            @RequestParam(defaultValue = "vehicle") String tab,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            Model model) {

        List<Vehicle> vehicles = (q != null && !q.isBlank())
                ? vehicleRepository.filterVehicles(q.trim(), null, null, status, parseStatus(status))
                : vehicleRepository.findAll();

        model.addAttribute("tab", tab);
        model.addAttribute("q", q);
        model.addAttribute("statusFilter", status);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("reservations", reservationRepository.findAll());
        model.addAttribute("rentals", rentalRepository.findAll());
        model.addAttribute("categories", vehicleCategoryRepository.findAll());
        model.addAttribute("newVehicle", new Vehicle());
        model.addAttribute("newCustomer", new Customer());
        model.addAttribute("vehicleStatuses", VehicleStatus.values());
        model.addAttribute("fuelTypes", FuelType.values());
        model.addAttribute("transmissionTypes", TransmissionType.values());
        // Already added in 1.1 above — confirm this line exists:
        model.addAttribute("insurancePlans", insurancePlanRepository.findByIsActiveTrue());
        return "storage/index";
    }

    // ── CREATE VEHICLE ─────────────────────────────────────────

    @PostMapping("/vehicles/create")
    public String createVehicle(@ModelAttribute Vehicle vehicle,
                                @RequestParam Integer categoryId,
                                RedirectAttributes ra) {
        try {
            VehicleCategory cat = vehicleCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            vehicle.setCategory(cat);
            vehicleRepository.save(vehicle);
            ra.addFlashAttribute("successMsg", "Thêm xe thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/storage?tab=vehicle";
    }

    // ── UPDATE VEHICLE ─────────────────────────────────────────

    @PostMapping("/vehicles/{id}/update")
    public String updateVehicle(@PathVariable Integer id,
                                @ModelAttribute Vehicle vehicle,
                                @RequestParam Integer categoryId,
                                RedirectAttributes ra) {
        try {
            Vehicle existing = vehicleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Xe không tồn tại"));
            VehicleCategory cat = vehicleCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            vehicle.setVehicleId(id);
            vehicle.setCategory(cat);
            vehicle.setCreatedAt(existing.getCreatedAt());
            vehicleRepository.save(vehicle);
            ra.addFlashAttribute("successMsg", "Cập nhật xe thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/storage?tab=vehicle";
    }

    // ── DELETE VEHICLE ─────────────────────────────────────────

    @PostMapping("/vehicles/{id}/delete")
    public String deleteVehicle(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            vehicleRepository.deleteById(id);
            ra.addFlashAttribute("successMsg", "Xóa xe thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/storage?tab=vehicle";
    }

    // ── CREATE CUSTOMER ────────────────────────────────────────

    @PostMapping("/customers/create")
    public String createCustomer(@ModelAttribute Customer customer, RedirectAttributes ra) {
        try {
            customerRepository.save(customer);
            ra.addFlashAttribute("successMsg", "Thêm khách hàng thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/storage?tab=customer";
    }

    // ── UPDATE CUSTOMER ────────────────────────────────────────

    @PostMapping("/customers/{id}/update")
    public String updateCustomer(@PathVariable Integer id,
                                 @ModelAttribute Customer customer,
                                 RedirectAttributes ra) {
        try {
            Customer existing = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Khách hàng không tồn tại"));
            customer.setCustomerId(id);
            customer.setCreatedAt(existing.getCreatedAt());
            customerRepository.save(customer);
            ra.addFlashAttribute("successMsg", "Cập nhật khách hàng thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/storage?tab=customer";
    }

    // ── DELETE CUSTOMER ────────────────────────────────────────

    @PostMapping("/customers/{id}/delete")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            customerRepository.deleteById(id);
            ra.addFlashAttribute("successMsg", "Xóa khách hàng thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/storage?tab=customer";
    }

    // ── CREATE RESERVATION ─────────────────────────────────────

    @PostMapping("/reservations/create")
    public String createReservation(@ModelAttribute Reservation reservation,
                                    @RequestParam Integer customerId,
                                    @RequestParam Integer vehicleId,
                                    @RequestParam(required = false) Integer insurancePlanId,
                                    RedirectAttributes ra) {
        try {
            reservation.setCustomer(customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Khách hàng không tồn tại")));
            reservation.setVehicle(vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new IllegalArgumentException("Xe không tồn tại")));
            if (insurancePlanId != null) {
                reservation.setInsurancePlan(insurancePlanRepository.findById(insurancePlanId).orElse(null));
            }
            reservationRepository.save(reservation);
            ra.addFlashAttribute("successMsg", "Tạo đặt xe thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/storage?tab=reservation";
    }

    // ── UPDATE RESERVATION STATUS ──────────────────────────────

    @PostMapping("/reservations/{id}/status")
    public String updateReservationStatus(@PathVariable Integer id,
                                          @RequestParam ReservationStatus status,
                                          RedirectAttributes ra) {
        try {
            Reservation r = reservationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Đặt xe không tồn tại"));
            r.setStatus(status);
            if (status == ReservationStatus.Cancelled) r.setCancelledAt(Instant.now());
            reservationRepository.save(r);
            ra.addFlashAttribute("successMsg", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/storage?tab=reservation";
    }

    private VehicleStatus parseStatus(String s) {
        if (s == null || s.isBlank()) return null;
        try { return VehicleStatus.valueOf(s); } catch (Exception e) { return null; }
    }
    @GetMapping("/vehicles/{id}/data")
@ResponseBody
public ResponseEntity<?> getVehicleData(@PathVariable Integer id) {
    return vehicleRepository.findById(id)
            .map(v -> ResponseEntity.ok(v))
            .orElse(ResponseEntity.notFound().build());
}

@GetMapping("/customers/{id}/data")
@ResponseBody
public ResponseEntity<?> getCustomerData(@PathVariable Integer id) {
    return customerRepository.findById(id)
            .map(c -> ResponseEntity.ok(c))
            .orElse(ResponseEntity.notFound().build());
}
}