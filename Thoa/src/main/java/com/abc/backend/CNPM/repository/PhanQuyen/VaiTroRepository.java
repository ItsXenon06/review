package com.abc.backend.CNPM.repository.PhanQuyen;


import com.abc.backend.CNPM.model.PhanQuyen.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, Long> {

    boolean existsByTenVaiTro(String tenVaiTro);

    java.util.Optional<VaiTro> findByTenVaiTro(String tenVaiTro);
}