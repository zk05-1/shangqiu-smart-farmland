package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.PlantingRecord;
import com.sqnu.server.repository.PlantingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 种植记录服务层
 *
 * @author sqnu
 */
@Service
@Transactional
public class PlantingRecordService {

    @Autowired
    private PlantingRecordRepository plantingRecordRepository;

    /**
     * 分页查询种植记录
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    public PageResult<PlantingRecord> getPlantingRecordList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<PlantingRecord> recordPage = plantingRecordRepository.findByDeletedFalse(pageable);

        return new PageResult<>(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getNumber() + 1,
                recordPage.getSize()
        );
    }

    /**
     * 根据ID获取种植记录详情
     *
     * @param id 记录ID
     * @return 种植记录详情
     */
    public PlantingRecord getPlantingRecordById(Long id) {
        return plantingRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("种植记录不存在"));
    }

    /**
     * 根据农田ID查询种植记录
     *
     * @param farmlandId 农田ID
     * @return 种植记录列表
     */
    public List<PlantingRecord> getPlantingRecordsByFarmlandId(Long farmlandId) {
        return plantingRecordRepository.findByFarmlandId(farmlandId);
    }

    /**
     * 创建种植记录
     *
     * @param record 种植记录信息
     * @return 创建后的记录
     */
    public PlantingRecord createPlantingRecord(PlantingRecord record) {
        return plantingRecordRepository.save(record);
    }

    /**
     * 更新种植记录
     *
     * @param id     记录ID
     * @param record 更新信息
     * @return 更新后的记录
     */
    public PlantingRecord updatePlantingRecord(Long id, PlantingRecord record) {
        PlantingRecord existingRecord = getPlantingRecordById(id);

        if (record.getPlantDate() != null) {
            existingRecord.setPlantDate(record.getPlantDate());
        }
        if (record.getExpectHarvestDate() != null) {
            existingRecord.setExpectHarvestDate(record.getExpectHarvestDate());
        }
        if (record.getActualHarvestDate() != null) {
            existingRecord.setActualHarvestDate(record.getActualHarvestDate());
        }
        if (record.getPlantArea() != null) {
            existingRecord.setPlantArea(record.getPlantArea());
        }
        if (record.getSeedAmount() != null) {
            existingRecord.setSeedAmount(record.getSeedAmount());
        }
        if (record.getStatus() != null) {
            existingRecord.setStatus(record.getStatus());
        }
        if (record.getDescription() != null) {
            existingRecord.setDescription(record.getDescription());
        }

        return plantingRecordRepository.save(existingRecord);
    }

    /**
     * 删除种植记录（逻辑删除）
     *
     * @param id 记录ID
     */
    public void deletePlantingRecord(Long id) {
        PlantingRecord record = getPlantingRecordById(id);
        record.setDeleted(1);
        plantingRecordRepository.save(record);
    }
}