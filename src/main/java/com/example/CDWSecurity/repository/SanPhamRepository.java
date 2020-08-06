package com.example.CDWSecurity.repository;

import com.example.CDWSecurity.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SanPhamRepository extends JpaRepository<SanPham,Long> {
    @Query(value = "SELECT max(id) FROM SanPham ")
    Long findMaxId();
}
