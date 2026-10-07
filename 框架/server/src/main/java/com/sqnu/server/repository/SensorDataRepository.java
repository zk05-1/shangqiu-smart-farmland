package com.sqnu.server.repository;

import com.sqnu.server.entity.SensorData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    Page<SensorData> findByFarmlandId(Long farmlandId, Pageable pageable);

    List<SensorData> findTop10ByFarmlandIdOrderByCreatedTimeDesc(Long farmlandId);

    List<SensorData> findByFarmlandIdOrderByCreatedTimeDesc(Long farmlandId);
}