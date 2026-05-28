package com.abc.backend.CNPM.repository.PhanQuyen;


import com.abc.backend.CNPM.model.PhanQuyen.Quyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuyenRepository extends JpaRepository<Quyen, Long> {

    List<Quyen> findByVaiTroId(Long vaiTroId);

    Optional<Quyen> findByVaiTroIdAndDanhMucChucNang(Long vaiTroId, String danhMucChucNang);

    @Modifying
    @Query("DELETE FROM Quyen q WHERE q.vaiTro.id = :vaiTroId")
    void deleteByVaiTroId(@Param("vaiTroId") Long vaiTroId);
}