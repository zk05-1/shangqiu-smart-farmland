package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.PestMonitor;
import com.sqnu.server.repository.PestMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 病虫害监测服务层
 *
 * @author sqnu
 */
@Service
@Transactional
public class PestMonitorService {

    @Autowired
    private PestMonitorRepository pestMonitorRepository;

    /**
     * 分页查询病虫害监测记录
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    public PageResult<PestMonitor> getPestMonitorList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "foundDate"));
        Page<PestMonitor> recordPage = pestMonitorRepository.findByDeletedFalse(pageable);

        return new PageResult<>(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getNumber() + 1,
                recordPage.getSize()
        );
    }

    /**
     * 根据ID获取病虫害监测记录详情
     *
     * @param id 记录ID
     * @return 病虫害监测记录详情
     */
    public PestMonitor getPestMonitorById(Long id) {
        return pestMonitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("病虫害监测记录不存在"));
    }

    /**
     * 根据农田ID查询病虫害监测记录
     *
     * @param farmlandId 农田ID
     * @return 病虫害监测记录列表
     */
    public List<PestMonitor> getPestMonitorsByFarmlandId(Long farmlandId) {
        return pestMonitorRepository.findByFarmlandId(farmlandId);
    }

    /**
     * 创建病虫害监测记录
     *
     * @param record 病虫害监测记录信息
     * @return 创建后的记录
     */
    public PestMonitor createPestMonitor(PestMonitor record) {
        return pestMonitorRepository.save(record);
    }

    /**
     * 更新病虫害监测记录
     *
     * @param id     记录ID
     * @param record 更新信息
     * @return 更新后的记录
     */
    public PestMonitor updatePestMonitor(Long id, PestMonitor record) {
        PestMonitor existingRecord = getPestMonitorById(id);

        if (record.getPestName() != null) {
            existingRecord.setPestName(record.getPestName());
        }
        if (record.getPestType() != null) {
            existingRecord.setPestType(record.getPestType());
        }
        if (record.getSeverity() != null) {
            existingRecord.setSeverity(record.getSeverity());
        }
        if (record.getFoundDate() != null) {
            existingRecord.setFoundDate(record.getFoundDate());
        }
        if (record.getAffectedArea() != null) {
            existingRecord.setAffectedArea(record.getAffectedArea());
        }
        if (record.getImageUrl() != null) {
            existingRecord.setImageUrl(record.getImageUrl());
        }
        if (record.getDescription() != null) {
            existingRecord.setDescription(record.getDescription());
        }

        return pestMonitorRepository.save(existingRecord);
    }

    /**
     * 删除病虫害监测记录（逻辑删除）
     *
     * @param id 记录ID
     */
    public void deletePestMonitor(Long id) {
        PestMonitor record = getPestMonitorById(id);
        record.setDeleted(1);
        pestMonitorRepository.save(record);
    }
}