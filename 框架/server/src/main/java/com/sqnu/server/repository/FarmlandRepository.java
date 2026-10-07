package com.sqnu.server.repository;

import com.sqnu.server.entity.Farmland;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmlandRepository extends JpaRepository<Farmland, Long> {

    Farmland findByFarmlandCode(String farmlandCode);

    List<Farmland> findByStatus(String status);

    @Query("SELECT f FROM Farmland f WHERE f.deleted = 0 AND " +
           "(f.farmlandCode LIKE %:keyword% OR f.farmlandName LIKE %:keyword% OR f.location LIKE %:keyword%)")
    Page<Farmland> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    long countByStatusAndDeletedFalse(String status);

    Page<Farmland> findByDeletedFalse(Pageable pageable);
}