package com.sqnu.server.repository;

import com.sqnu.server.entity.PestMonitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 病虫害监测数据访问层
 *
 * @author sqnu
 */
@Repository
public interface PestMonitorRepository extends JpaRepository<PestMonitor, Long> {

    /**
     * 根据农田ID查询病虫害监测记录
     *
     * @param farmlandId 农田ID
     * @return 病虫害监测记录列表
     */
    List<PestMonitor> findByFarmlandId(Long farmlandId);

    /**
     * 分页查询病虫害监测记录
     *
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<PestMonitor> findByDeletedFalse(Pageable pageable);
}