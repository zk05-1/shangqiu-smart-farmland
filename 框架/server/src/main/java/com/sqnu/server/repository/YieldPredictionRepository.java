package com.sqnu.server.repository;

import com.sqnu.server.entity.YieldPrediction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产量预测数据访问层
 *
 * @author sqnu
 */
@Repository
public interface YieldPredictionRepository extends JpaRepository<YieldPrediction, Long> {

    /**
     * 根据农田ID查询产量预测
     *
     * @param farmlandId 农田ID
     * @return 产量预测列表
     */
    List<YieldPrediction> findByFarmlandId(Long farmlandId);

    /**
     * 分页查询产量预测
     *
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<YieldPrediction> findByDeletedFalse(Pageable pageable);
}