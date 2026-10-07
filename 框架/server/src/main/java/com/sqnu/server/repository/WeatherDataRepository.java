package com.sqnu.server.repository;

import com.sqnu.server.entity.WeatherData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 气象数据访问层
 *
 * @author sqnu
 */
@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    /**
     * 根据农田ID查询气象数据
     *
     * @param farmlandId 农田ID
     * @return 气象数据列表
     */
    List<WeatherData> findByFarmlandId(Long farmlandId);

    /**
     * 根据农田ID和日期查询气象数据
     *
     * @param farmlandId 农田ID
     * @param recordDate 记录日期
     * @return 气象数据
     */
    Optional<WeatherData> findByFarmlandIdAndRecordDate(Long farmlandId, LocalDate recordDate);

    /**
     * 分页查询气象数据
     *
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<WeatherData> findByDeletedFalse(Pageable pageable);
}