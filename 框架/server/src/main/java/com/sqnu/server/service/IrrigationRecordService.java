package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.IrrigationRecord;
import com.sqnu.server.repository.IrrigationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 灌溉记录服务层
 *
 * @author sqnu
 */
@Service
@Transactional
public class IrrigationRecordService {

    @Autowired
    private IrrigationRecordRepository irrigationRecordRepository;

    /**
     * 分页查询灌溉记录
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    public PageResult<IrrigationRecord> getIrrigationRecordList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "irrigateDate"));
        Page<IrrigationRecord> recordPage = irrigationRecordRepository.findByDeletedFalse(pageable);

        return new PageResult<>(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getNumber() + 1,
                recordPage.getSize()
        );
    }

    /**
     * 根据ID获取灌溉记录详情
     *
     * @param id 记录ID
     * @return 灌溉记录详情
     */
    public IrrigationRecord getIrrigationRecordById(Long id) {
        return irrigationRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("灌溉记录不存在"));
    }

    /**
     * 根据农田ID查询灌溉记录
     *
     * @param farmlandId 农田ID
     * @return 灌溉记录列表
     */
    public List<IrrigationRecord> getIrrigationRecordsByFarmlandId(Long farmlandId) {
        return irrigationRecordRepository.findByFarmlandId(farmlandId);
    }

    /**
     * 创建灌溉记录
     *
     * @param record 灌溉记录信息
     * @return 创建后的记录
     */
    public IrrigationRecord createIrrigationRecord(IrrigationRecord record) {
        return irrigationRecordRepository.save(record);
    }

    /**
     * 更新灌溉记录
     *
     * @param id     记录ID
     * @param record 更新信息
     * @return 更新后的记录
     */
    public IrrigationRecord updateIrrigationRecord(Long id, IrrigationRecord record) {
        IrrigationRecord existingRecord = getIrrigationRecordById(id);

        if (record.getIrrigateDate() != null) {
            existingRecord.setIrrigateDate(record.getIrrigateDate());
        }
        if (record.getWaterAmount() != null) {
            existingRecord.setWaterAmount(record.getWaterAmount());
        }
        if (record.getIrrigateMethod() != null) {
            existingRecord.setIrrigateMethod(record.getIrrigateMethod());
        }
        if (record.getIrrigateDuration() != null) {
            existingRecord.setIrrigateDuration(record.getIrrigateDuration());
        }
        if (record.getOperator() != null) {
            existingRecord.setOperator(record.getOperator());
        }
        if (record.getDescription() != null) {
            existingRecord.setDescription(record.getDescription());
        }

        return irrigationRecordRepository.save(existingRecord);
    }

    /**
     * 删除灌溉记录（逻辑删除）
     *
     * @param id 记录ID
     */
    public void deleteIrrigationRecord(Long id) {
        IrrigationRecord record = getIrrigationRecordById(id);
        record.setDeleted(1);
        irrigationRecordRepository.save(record);
    }
}