package com.abc.backend.CNPM.service;

import com.abc.backend.CNPM.service.NhaCungCapLuuTru;
import com.abc.backend.CNPM.model.Vehicle;
import com.abc.backend.CNPM.model.enums.VehicleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DichVuLuuTruXe {

    @Autowired
    private NhaCungCapLuuTru nhaCungCapLuuTru;

    public Map<String, Long> layThongKeBangDieuKhien() {
        return nhaCungCapLuuTru.layThongKe();
    }

    public List<Vehicle> layDanhSachXeDaLoc(String tuKhoa, String hangXe, String moHinh, String trangThai) {
        return nhaCungCapLuuTru.timKiemVaLocXe(tuKhoa, hangXe, moHinh, trangThai);
    }

    public Vehicle themXeMoi(Vehicle xe) {
        // Dùng status thay vì createdDate vì Vehicle không có field createdDate
        if (xe.getStatus() == null) {
            xe.setStatus(VehicleStatus.Available);
        }
        return nhaCungCapLuuTru.luuXe(xe);
    }

    public void xoaXe(Integer id) {
        nhaCungCapLuuTru.xoaXe(id);
    }
}