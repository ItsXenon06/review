package com.abc.backend.CNPM.service;

import com.abc.backend.CNPM.dto.*;
import com.abc.backend.CNPM.model.InsurancePlan;
import com.abc.backend.CNPM.model.Vehicle;
import com.abc.backend.CNPM.repository.InsurancePlanRepository;
import com.abc.backend.CNPM.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abc.backend.CNPM.dto.PenaltyResultDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculationService {

    private static final BigDecimal VAT_RATE           = new BigDecimal("0.08");
    private static final BigDecimal CLEANING_FEE       = new BigDecimal("100000");
    private static final BigDecimal OVER_KM_FEE_PER_KM = new BigDecimal("2000");
    private static final int        KM_PER_DAY_LIMIT   = 200;
    private static final BigDecimal LATE_PENALTY_RATE  = new BigDecimal("0.50"); // 50% of next-day rate

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private InsurancePlanRepository insurancePlanRepository;

    // -------------------------------------------------------------------------
    //  TAB 1 – Rental Cost
    // -------------------------------------------------------------------------
    public CalculationResultDTO calculateRentalCost(CalculationRequestDTO req) {

        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Xe không tồn tại"));

        BigDecimal dailyRate = vehicle.getCategory().getBaseDailyRate();

        // Duration
        LocalDateTime start = LocalDateTime.of(req.getStartDate(), req.getStartTime() != null ? req.getStartTime() : java.time.LocalTime.of(8, 0));
        LocalDateTime end   = LocalDateTime.of(req.getEndDate(),   req.getEndTime()   != null ? req.getEndTime()   : java.time.LocalTime.of(8, 0));

        long totalHours = Duration.between(start, end).toHours();
        if (totalHours <= 0) throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");

        // LẤY SỐ NGÀY VÀ GIỜ THỰC TẾ ĐỂ HIỂN THỊ LÊN UI
        int actualDays = (int) (totalHours / 24); // 74 giờ / 24 = 3 ngày
        int extraHours = (int) (totalHours % 24); // 74 giờ % 24 = 2 giờ

        // TÍNH SỐ NGÀY ĐỂ NHÂN TIỀN (Áp dụng chính sách làm tròn nếu có giờ lẻ)
        int billedDays = (extraHours > 0) ? (actualDays + 1) : actualDays;
        if (billedDays == 0) billedDays = 1;

        // Base rental
        BigDecimal baseCharge = dailyRate.multiply(BigDecimal.valueOf(billedDays));

        // Insurance
        BigDecimal insuranceDailyRate = BigDecimal.ZERO;
        BigDecimal insuranceCharge    = BigDecimal.ZERO;
        if (req.getInsurancePlanId() != null) {
            InsurancePlan plan = insurancePlanRepository.findById(req.getInsurancePlanId()).orElse(null);
            if (plan != null) {
                insuranceDailyRate = plan.getDailyPremium();
                insuranceCharge    = insuranceDailyRate.multiply(BigDecimal.valueOf(billedDays));
            }
        }

        // Over-km
        int totalIncludedKm = KM_PER_DAY_LIMIT * billedDays;
        int estimatedKm     = req.getEstimatedKm() != null ? req.getEstimatedKm() : 0;
        int overKm          = Math.max(0, estimatedKm - totalIncludedKm);
        BigDecimal overKmCharge = OVER_KM_FEE_PER_KM.multiply(BigDecimal.valueOf(overKm));

        // Subtotal (excluding VAT)
        BigDecimal subtotal = baseCharge
                .add(insuranceCharge)
                .add(overKmCharge)
                .add(CLEANING_FEE);

        BigDecimal vatAmount   = subtotal.multiply(VAT_RATE).setScale(0, RoundingMode.HALF_UP);
        BigDecimal totalCharge = subtotal.add(vatAmount);

        BigDecimal deposit        = req.getDeposit() != null ? req.getDeposit() : BigDecimal.ZERO;
        BigDecimal remainingAmount = totalCharge.subtract(deposit);

        return CalculationResultDTO.builder()
                .vehicleDisplayName(vehicle.getMake() + " " + vehicle.getModel() + " " + vehicle.getYear())
                .licensePlate(vehicle.getLicensePlate())
                .vehicleType(vehicle.getSeatingCapacity() + " chỗ")
                .dailyRate(dailyRate)

                // --- THỜI GIAN THỰC TẾ (Hiện panel phải: "3 ngày 2 giờ") ---
                .totalDays(actualDays)
                .extraHours(extraHours)

                // --- SỐ NGÀY TÍNH TIỀN THỰC TẾ (Thêm vào nếu DTO có, để truyền xuống UI hiển thị "4 ngày") ---
                // .billedDays(billedDays)

                .rentalType("SELF_DRIVE".equals(req.getRentalType()) ? "Tự lái" : "Có tài xế")
                .estimatedKm(estimatedKm)
                .includedKmPerDay(KM_PER_DAY_LIMIT)
                .totalIncludedKm(totalIncludedKm)
                .overKm(overKm)
                .overKmFeePerKm(OVER_KM_FEE_PER_KM)
                .overKmCharge(overKmCharge)

                // Các khoản chi phí đã được tính bằng 'billedDays' cục bộ cực chuẩn phía trên
                .baseRentalCharge(baseCharge)
                .insuranceDailyRate(insuranceDailyRate)
                .insuranceCharge(insuranceCharge)

                .cleaningFee(CLEANING_FEE)
                .subtotal(subtotal)
                .vatRate(VAT_RATE)
                .vatAmount(vatAmount)
                .totalCharge(totalCharge)
                .deposit(deposit)
                .remainingAmount(remainingAmount)
                .cancellationPolicy("Hủy trước 24h: miễn phí. Sau 24h: tính 30% tổng tiền thuê.")
                .build();
    }

    // -------------------------------------------------------------------------
    //  TAB 2 – Penalty Fee
    // -------------------------------------------------------------------------
    public PenaltyResultDTO calculatePenalty(PenaltyRequestDTO req) {

        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Xe không tồn tại"));

        LocalDateTime agreed = LocalDateTime.of(req.getAgreedReturnDate(),
                req.getAgreedReturnTime() != null ? req.getAgreedReturnTime() : java.time.LocalTime.of(8, 0));
        LocalDateTime actual = LocalDateTime.of(req.getActualReturnDate(),
                req.getActualReturnTime() != null ? req.getActualReturnTime() : java.time.LocalTime.of(8, 0));

        long lateHours = Duration.between(agreed, actual).toHours();
        long lateDays  = 0;
        BigDecimal lateReturnFee = BigDecimal.ZERO;

        if (lateHours > 0) {
            lateDays = (lateHours / 24) + (lateHours % 24 > 0 ? 1 : 0);
            // 50% of daily rate per late day
            lateReturnFee = req.getDailyRate()
                    .multiply(LATE_PENALTY_RATE)
                    .multiply(BigDecimal.valueOf(lateDays))
                    .setScale(0, RoundingMode.HALF_UP);
        }

        // Over-km fee
        int overKm = Math.max(0, req.getActualKm() - req.getIncludedKm());
        BigDecimal overKmFee = OVER_KM_FEE_PER_KM
                .multiply(BigDecimal.valueOf(overKm))
                .setScale(0, RoundingMode.HALF_UP);

        BigDecimal totalPenalty = lateReturnFee.add(overKmFee);

        StringBuilder note = new StringBuilder();
        if (lateHours > 0) {
            note.append("Trả trễ ").append(lateHours).append(" giờ (").append(lateDays).append(" ngày tính phí). ");
        } else {
            note.append("Trả đúng hạn. ");
        }
        if (overKm > 0) {
            note.append("Vượt ").append(overKm).append(" km × 2,000 đ/km.");
        }

        return PenaltyResultDTO.builder()
                .vehicleDisplayName(vehicle.getMake() + " " + vehicle.getModel() + " " + vehicle.getYear())
                .licensePlate(vehicle.getLicensePlate())
                .lateHours(Math.max(0, lateHours))
                .lateDays(lateDays)
                .lateReturnFee(lateReturnFee)
                .overKm(overKm)
                .overKmFee(overKmFee)
                .totalPenalty(totalPenalty)
                .breakdownNote(note.toString())
                .build();
    }

    // -------------------------------------------------------------------------
    //  TAB 3 – Depreciation
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
//  TAB 3 – Depreciation (ĐÃ SỬA LOGIC CHUẨN)
// -------------------------------------------------------------------------
    public DepreciationResultDTO calculateDepreciation(DepreciationRequestDTO req) {

        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Xe không tồn tại"));

        BigDecimal original = req.getOriginalValue();
        int        years    = req.getVehicleAgeYears();

        // 1. CHUẨN HÓA TỶ LỆ: Chia cho 100 để đổi từ phần trăm (ví dụ: 50) thành hệ số thập phân (0.5)
        BigDecimal rawRate = req.getAnnualDepreciationRate(); // Đây là con số 50 người dùng nhập
        BigDecimal decimalRate = rawRate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP); // Biến thành 0.5000

        // 2. TÍNH KHẤU HAO THEO HỆ SỐ ĐÃ CHUẨN HÓA
        BigDecimal annualDepreciation = original.multiply(decimalRate).setScale(0, RoundingMode.HALF_UP); // 500tr * 0.5 = 250tr
        BigDecimal totalDepreciation  = annualDepreciation.multiply(BigDecimal.valueOf(years));

        // Giá trị tối thiểu không được dưới 10% nguyên giá
        BigDecimal minValue = original.multiply(new BigDecimal("0.10")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal currentValue = original.subtract(totalDepreciation);

        if (currentValue.compareTo(minValue) < 0) {
            currentValue = minValue;
            totalDepreciation = original.subtract(minValue);
        }

        BigDecimal depreciationPercent = totalDepreciation
                .divide(original, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);

        // Vòng lặp chi tiết từng năm
        List<DepreciationResultDTO.YearRow> rows = new ArrayList<>();
        BigDecimal currentBookValue = original;

        for (int y = 1; y <= years; y++) {
            BigDecimal depAmountOfThisYear = annualDepreciation;
            BigDecimal nextBookValue = currentBookValue.subtract(depAmountOfThisYear);

            if (nextBookValue.compareTo(minValue) < 0) {
                depAmountOfThisYear = currentBookValue.subtract(minValue);
                nextBookValue = minValue;
            }

            rows.add(DepreciationResultDTO.YearRow.builder()
                    .year(vehicle.getYear() + y - 1)
                    .depreciationAmount(depAmountOfThisYear)
                    .bookValue(nextBookValue)
                    .build());

            currentBookValue = nextBookValue;

            if (currentBookValue.compareTo(minValue) <= 0) {
                break;
            }
        }

        return DepreciationResultDTO.builder()
                .vehicleDisplayName(vehicle.getMake() + " " + vehicle.getModel() + " " + vehicle.getYear())
                .licensePlate(vehicle.getLicensePlate())
                .vehicleYear(vehicle.getYear())
                .originalValue(original)
                .totalDepreciationAmount(totalDepreciation)
                .currentEstimatedValue(currentValue)
                .depreciationPercent(depreciationPercent)
                .vehicleAgeYears(years)
                // SỬA TẠI ĐÂY: Trả về chính con số gốc rawRate (50) vì giao diện đã có ký tự % rồi
                .annualDepreciationRate(rawRate.setScale(0, RoundingMode.HALF_UP))
                .annualDepreciationAmount(annualDepreciation)
                .yearlyBreakdown(rows)
                .build();
    }
}