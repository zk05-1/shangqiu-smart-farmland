package com.sqnu.server.repository;

import com.sqnu.server.entity.FertilizationRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 施肥记录数据访问层
 *
 * @author sqnu
 */
@Repository
public interface FertilizationRecordRepository extends JpaRepository<FertilizationRecord, Long> {

    /**
     * 根据农田ID查询施肥记录
     *
     * @param farmlandId 农田ID
     * @return 施肥记录列表
     */
    List<FertilizationRecord> findByFarmlandId(Long farmlandId);

    /**
     * 分页查询施肥记录
     *
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<FertilizationRecord> findByDeletedFalse(Pageable pageable);
}