package com.sqnu.server.repository;

import com.sqnu.server.entity.PlantingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 种植记录数据访问层
 *
 * @author sqnu
 */
@Repository
public interface PlantingRecordRepository extends JpaRepository<PlantingRecord, Long> {

    /**
     * 根据农田ID查询种植记录
     *
     * @param farmlandId 农田ID
     * @return 种植记录列表
     */
    List<PlantingRecord> findByFarmlandId(Long farmlandId);

    /**
     * 根据状态查询种植记录
     *
     * @param status 状态
     * @return 种植记录列表
     */
    List<PlantingRecord> findByStatus(String status);

    /**
     * 分页查询种植记录
     *
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<PlantingRecord> findByDeletedFalse(Pageable pageable);
}