package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.FertilizationRecord;
import com.sqnu.server.repository.FertilizationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 施肥记录服务层
 *
 * @author sqnu
 */
@Service
@Transactional
public class FertilizationRecordService {

    @Autowired
    private FertilizationRecordRepository fertilizationRecordRepository;

    /**
     * 分页查询施肥记录
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    public PageResult<FertilizationRecord> getFertilizationRecordList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "applyDate"));
        Page<FertilizationRecord> recordPage = fertilizationRecordRepository.findByDeletedFalse(pageable);

        return new PageResult<>(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getNumber() + 1,
                recordPage.getSize()
        );
    }

    /**
     * 根据ID获取施肥记录详情
     *
     * @param id 记录ID
     * @return 施肥记录详情
     */
    public FertilizationRecord getFertilizationRecordById(Long id) {
        return fertilizationRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("施肥记录不存在"));
    }

    /**
     * 根据农田ID查询施肥记录
     *
     * @param farmlandId 农田ID
     * @return 施肥记录列表
     */
    public List<FertilizationRecord> getFertilizationRecordsByFarmlandId(Long farmlandId) {
        return fertilizationRecordRepository.findByFarmlandId(farmlandId);
    }

    /**
     * 创建施肥记录
     *
     * @param record 施肥记录信息
     * @return 创建后的记录
     */
    public FertilizationRecord createFertilizationRecord(FertilizationRecord record) {
        return fertilizationRecordRepository.save(record);
    }

    /**
     * 更新施肥记录
     *
     * @param id     记录ID
     * @param record 更新信息
     * @return 更新后的记录
     */
    public FertilizationRecord updateFertilizationRecord(Long id, FertilizationRecord record) {
        FertilizationRecord existingRecord = getFertilizationRecordById(id);

        if (record.getFertilizerName() != null) {
            existingRecord.setFertilizerName(record.getFertilizerName());
        }
        if (record.getFertilizerType() != null) {
            existingRecord.setFertilizerType(record.getFertilizerType());
        }
        if (record.getApplyDate() != null) {
            existingRecord.setApplyDate(record.getApplyDate());
        }
        if (record.getAmount() != null) {
            existingRecord.setAmount(record.getAmount());
        }
        if (record.getApplyMethod() != null) {
            existingRecord.setApplyMethod(record.getApplyMethod());
        }
        if (record.getOperator() != null) {
            existingRecord.setOperator(record.getOperator());
        }
        if (record.getDescription() != null) {
            existingRecord.setDescription(record.getDescription());
        }

        return fertilizationRecordRepository.save(existingRecord);
    }

    /**
     * 删除施肥记录（逻辑删除）
     *
     * @param id 记录ID
     */
    public void deleteFertilizationRecord(Long id) {
        FertilizationRecord record = getFertilizationRecordById(id);
        record.setDeleted(1);
        fertilizationRecordRepository.save(record);
    }
}