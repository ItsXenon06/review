package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.model.Customer;
import com.abc.backend.CNPM.model.Reservation;
import com.abc.backend.CNPM.model.Vehicle;
import com.abc.backend.CNPM.repository.CustomerRepository;
import com.abc.backend.CNPM.repository.ReservationRepository;
import com.abc.backend.CNPM.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final VehicleRepository     vehicleRepository;
    private final CustomerRepository    customerRepository;
    private final ReservationRepository reservationRepository;

    @GetMapping
    public String searchPage(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false, defaultValue = "vehicle") String tab,
            Model model) {

        model.addAttribute("q",   q);
        model.addAttribute("tab", tab);

        if (q.isBlank()) {
            model.addAttribute("vehicles",     Collections.emptyList());
            model.addAttribute("customers",    Collections.emptyList());
            model.addAttribute("reservations", Collections.emptyList());
            return "search/index";
        }

        String keyword = q.trim().toLowerCase();

        // Vehicle search — reuse the existing filterVehicles query
        List<Vehicle> vehicles = vehicleRepository.filterVehicles(
                keyword, null, null, null, null);
        model.addAttribute("vehicles", vehicles);

        // Customer search — filter in memory (small dataset expected)
        List<Customer> customers = customerRepository.findAll().stream()
                .filter(c ->
                    c.getEmail().toLowerCase().contains(keyword)
                    || c.getFirstName().toLowerCase().contains(keyword)
                    || c.getLastName().toLowerCase().contains(keyword)
                    || (c.getDriverLicenseNo() != null
                        && c.getDriverLicenseNo().toLowerCase().contains(keyword))
                )
                .collect(Collectors.toList());
        model.addAttribute("customers", customers);

        // Reservation search — filter by vehicle plate or customer name/email
        List<Reservation> reservations = reservationRepository.findAll().stream()
                .filter(r ->
                    r.getVehicle().getLicensePlate().toLowerCase().contains(keyword)
                    || r.getCustomer().getEmail().toLowerCase().contains(keyword)
                    || r.getCustomer().getFirstName().toLowerCase().contains(keyword)
                    || r.getCustomer().getLastName().toLowerCase().contains(keyword)
                )
                .collect(Collectors.toList());
        model.addAttribute("reservations", reservations);

        return "search/index";
    }
}