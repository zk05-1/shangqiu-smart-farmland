package com.sqnu.server.repository;

import com.sqnu.server.entity.PestControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 病虫害防治数据访问层
 *
 * @author sqnu
 */
@Repository
public interface PestControlRepository extends JpaRepository<PestControl, Long> {

    /**
     * 根据病虫害监测ID查询防治记录
     *
     * @param pestMonitorId 病虫害监测ID
     * @return 防治记录列表
     */
    List<PestControl> findByPestMonitorId(Long pestMonitorId);

    /**
     * 分页查询防治记录
     *
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<PestControl> findByDeletedFalse(Pageable pageable);
}