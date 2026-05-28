package com.abc.backend.CNPM.service;

import com.abc.backend.CNPM.model.Vehicle;
import com.abc.backend.CNPM.model.enums.VehicleStatus;
import com.abc.backend.CNPM.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Triển khai NhaCungCapLuuTru sử dụng cơ sở dữ liệu SQL (JPA/Hibernate)
 */
@Component
public class NhaCungCapLuuTruSQL implements NhaCungCapLuuTru {

    @Autowired
    private VehicleRepository khoXe;

    /**
     * Lấy toàn bộ danh sách xe từ cơ sở dữ liệu
     */
    @Override
    public List<Vehicle> layTatCaXe() {
        return khoXe.findAll();
    }

    /**
     * Tìm kiếm và lọc xe theo từ khóa, hãng, mô hình và trạng thái
     * Chuẩn hóa dữ liệu đầu vào trước khi đưa vào câu lệnh SQL
     */
    @Override
    public List<Vehicle> timKiemVaLocXe(String tuKhoa, String hangXe, String moHinh, String trangThai) {
        String thamSoTimKiem   = (tuKhoa   != null && !tuKhoa.trim().isEmpty())              ? tuKhoa.trim() : null;
        String thamSoHang      = (hangXe   != null && !hangXe.equalsIgnoreCase("Tất cả"))    ? hangXe        : null;
        String thamSoMoHinh    = (moHinh   != null && !moHinh.equalsIgnoreCase("Tất cả"))    ? moHinh        : null;
        String thamSoTrangThai = (trangThai != null && !trangThai.equalsIgnoreCase("Tất cả")) ? trangThai     : null;

        VehicleStatus statusEnum = null;
if (thamSoTrangThai != null) {
    try { statusEnum = VehicleStatus.valueOf(thamSoTrangThai); } catch (IllegalArgumentException ignored) {}
}
return khoXe.filterVehicles(thamSoTimKiem, thamSoHang, thamSoMoHinh, thamSoTrangThai, statusEnum);
    }

    /**
     * Lấy thống kê số lượng xe theo từng trạng thái
     * Sử dụng đúng tên enum: Available, Rented, Maintenance, Retired
     */
    @Override
    public Map<String, Long> layThongKe() {
        Map<String, Long> thongKe = new HashMap<>();

        thongKe.put("tongSoXe",      khoXe.count());
        thongKe.put("xeSanSang",     khoXe.countByStatus(VehicleStatus.Available));
        thongKe.put("xeDangThue",    khoXe.countByStatus(VehicleStatus.Rented));
        thongKe.put("xeBaoDuong",    khoXe.countByStatus(VehicleStatus.Maintenance));
        thongKe.put("xeNgungHoatDong", khoXe.countByStatus(VehicleStatus.Retired));

        return thongKe;
    }

    /**
     * Lưu thông tin xe vào cơ sở dữ liệu (thêm mới hoặc cập nhật)
     */
    @Override
    public Vehicle luuXe(Vehicle xe) {
        return khoXe.save(xe);
    }

    /**
     * Xóa xe khỏi cơ sở dữ liệu theo ID
     */
    @Override
    public void xoaXe(Integer id) {
    khoXe.deleteById(id);
}
}