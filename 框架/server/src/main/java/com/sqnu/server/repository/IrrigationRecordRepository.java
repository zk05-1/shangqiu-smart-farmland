package com.sqnu.server.repository;

import com.sqnu.server.entity.IrrigationRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 灌溉记录数据访问层
 *
 * @author sqnu
 */
@Repository
public interface IrrigationRecordRepository extends JpaRepository<IrrigationRecord, Long> {

    /**
     * 根据农田ID查询灌溉记录
     *
     * @param farmlandId 农田ID
     * @return 灌溉记录列表
     */
    List<IrrigationRecord> findByFarmlandId(Long farmlandId);

    /**
     * 分页查询灌溉记录
     *
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<IrrigationRecord> findByDeletedFalse(Pageable pageable);
}