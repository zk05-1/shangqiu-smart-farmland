package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.PestControl;
import com.sqnu.server.repository.PestControlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 病虫害防治服务层
 *
 * @author sqnu
 */
@Service
@Transactional
public class PestControlService {

    @Autowired
    private PestControlRepository pestControlRepository;

    /**
     * 分页查询病虫害防治记录
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    public PageResult<PestControl> getPestControlList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "controlDate"));
        Page<PestControl> recordPage = pestControlRepository.findByDeletedFalse(pageable);

        return new PageResult<>(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getNumber() + 1,
                recordPage.getSize()
        );
    }

    /**
     * 根据ID获取病虫害防治记录详情
     *
     * @param id 记录ID
     * @return 病虫害防治记录详情
     */
    public PestControl getPestControlById(Long id) {
        return pestControlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("病虫害防治记录不存在"));
    }

    /**
     * 根据病虫害监测ID查询防治记录
     *
     * @param pestMonitorId 病虫害监测ID
     * @return 病虫害防治记录列表
     */
    public List<PestControl> getPestControlsByPestMonitorId(Long pestMonitorId) {
        return pestControlRepository.findByPestMonitorId(pestMonitorId);
    }

    /**
     * 创建病虫害防治记录
     *
     * @param record 病虫害防治记录信息
     * @return 创建后的记录
     */
    public PestControl createPestControl(PestControl record) {
        return pestControlRepository.save(record);
    }

    /**
     * 更新病虫害防治记录
     *
     * @param id     记录ID
     * @param record 更新信息
     * @return 更新后的记录
     */
    public PestControl updatePestControl(Long id, PestControl record) {
        PestControl existingRecord = getPestControlById(id);

        if (record.getControlMethod() != null) {
            existingRecord.setControlMethod(record.getControlMethod());
        }
        if (record.getPesticideName() != null) {
            existingRecord.setPesticideName(record.getPesticideName());
        }
        if (record.getDosage() != null) {
            existingRecord.setDosage(record.getDosage());
        }
        if (record.getControlDate() != null) {
            existingRecord.setControlDate(record.getControlDate());
        }
        if (record.getEffect() != null) {
            existingRecord.setEffect(record.getEffect());
        }
        if (record.getOperator() != null) {
            existingRecord.setOperator(record.getOperator());
        }
        if (record.getCost() != null) {
            existingRecord.setCost(record.getCost());
        }
        if (record.getDescription() != null) {
            existingRecord.setDescription(record.getDescription());
        }

        return pestControlRepository.save(existingRecord);
    }

    /**
     * 删除病虫害防治记录（逻辑删除）
     *
     * @param id 记录ID
     */
    public void deletePestControl(Long id) {
        PestControl record = getPestControlById(id);
        record.setDeleted(1);
        pestControlRepository.save(record);
    }
}