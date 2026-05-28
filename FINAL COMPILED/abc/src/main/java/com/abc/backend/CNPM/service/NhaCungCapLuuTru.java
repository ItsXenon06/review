package com.abc.backend.CNPM.service;

import com.abc.backend.CNPM.model.Vehicle;

import java.util.List;
import java.util.Map;

/**
 * Interface định nghĩa các thao tác lưu trữ xe
 */
public interface NhaCungCapLuuTru {

    List<Vehicle> layTatCaXe();

    List<Vehicle> timKiemVaLocXe(String tuKhoa, String hangXe, String moHinh, String trangThai);

    Map<String, Long> layThongKe();

    Vehicle luuXe(Vehicle xe);

    void xoaXe(Long id);
}