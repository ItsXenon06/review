package com.abc.backend.CNPM.dto.request;

import lombok.Data;
import java.util.Map;
import java.util.Set;

@Data
public class PhanQuyenRq {
    private String              tenVaiTro;
    private String              moTa;
    private Map<String, Set<String>> danhSachQuyen;  // key = DanhMucChucNang, value = {"XEM","THEM",...}
    private Long                nguoiDungId;
    private Long                vaiTroId;
    private Long                vaiTroNguonId;
    private Long                vaiTroDoId;
}
