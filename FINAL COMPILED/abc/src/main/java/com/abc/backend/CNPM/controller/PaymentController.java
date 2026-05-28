package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.model.Payment;
import com.abc.backend.CNPM.model.enums.PaymentMethod;
import com.abc.backend.CNPM.model.enums.PaymentType;
import com.abc.backend.CNPM.repository.PaymentRepository;
import com.abc.backend.CNPM.repository.RentalRepository;
import com.abc.backend.CNPM.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final RentalRepository rentalRepository;

    @GetMapping
    public String paymentsPage(
            @RequestParam(required = false) String type,
            Model model) {

        List<Payment> payments = (type != null && !type.isBlank())
                ? paymentRepository.findByPaymentType(PaymentType.valueOf(type))
                : paymentRepository.findAll();

        BigDecimal totalCharge  = payments.stream()
                .filter(p -> p.getPaymentType() == PaymentType.Charge)
                .map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDeposit = payments.stream()
                .filter(p -> p.getPaymentType() == PaymentType.Deposit)
                .map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalRefund  = payments.stream()
                .filter(p -> p.getPaymentType() == PaymentType.Refund)
                .map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("payments", payments);
        model.addAttribute("typeFilter", type);
        model.addAttribute("totalCharge", totalCharge);
        model.addAttribute("totalDeposit", totalDeposit);
        model.addAttribute("totalRefund", totalRefund);
        model.addAttribute("paymentMethods", PaymentMethod.values());
        model.addAttribute("paymentTypes", PaymentType.values());
        model.addAttribute("reservations", reservationRepository.findAll());
        model.addAttribute("rentals", rentalRepository.findAll());
        return "payments/index";
    }

    @PostMapping("/create")
    public String createPayment(
            @RequestParam(required = false) Integer reservationId,
            @RequestParam(required = false) Integer rentalId,
            @RequestParam BigDecimal amount,
            @RequestParam PaymentMethod paymentMethod,
            @RequestParam PaymentType paymentType,
            @RequestParam(required = false) String referenceNo,
            @RequestParam(required = false) String notes,
            RedirectAttributes ra) {
        try {
            if (reservationId == null && rentalId == null)
                throw new IllegalArgumentException("Phải chọn đặt xe hoặc thuê xe");

            Payment p = Payment.builder()
                    .amount(amount)
                    .paymentMethod(paymentMethod)
                    .paymentType(paymentType)
                    .referenceNo(referenceNo)
                    .notes(notes)
                    .paymentDate(Instant.now())
                    .build();

            if (reservationId != null)
                p.setReservation(reservationRepository.findById(reservationId).orElseThrow());
            if (rentalId != null)
                p.setRental(rentalRepository.findById(rentalId).orElseThrow());

            paymentRepository.save(p);
            ra.addFlashAttribute("successMsg", "Tạo thanh toán thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/payments";
    }
}