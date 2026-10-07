package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.YieldPrediction;
import com.sqnu.server.repository.YieldPredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产量预测服务层
 *
 * @author sqnu
 */
@Service
@Transactional
public class YieldPredictionService {

    @Autowired
    private YieldPredictionRepository yieldPredictionRepository;

    /**
     * 分页查询产量预测记录
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    public PageResult<YieldPrediction> getYieldPredictionList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<YieldPrediction> recordPage = yieldPredictionRepository.findByDeletedFalse(pageable);

        return new PageResult<>(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getNumber() + 1,
                recordPage.getSize()
        );
    }

    /**
     * 根据ID获取产量预测记录详情
     *
     * @param id 记录ID
     * @return 产量预测记录详情
     */
    public YieldPrediction getYieldPredictionById(Long id) {
        return yieldPredictionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产量预测记录不存在"));
    }

    /**
     * 根据农田ID查询产量预测记录
     *
     * @param farmlandId 农田ID
     * @return 产量预测记录列表
     */
    public List<YieldPrediction> getYieldPredictionsByFarmlandId(Long farmlandId) {
        return yieldPredictionRepository.findByFarmlandId(farmlandId);
    }

    /**
     * 创建产量预测记录
     *
     * @param prediction 产量预测记录信息
     * @return 创建后的记录
     */
    public YieldPrediction createYieldPrediction(YieldPrediction prediction) {
        return yieldPredictionRepository.save(prediction);
    }

    /**
     * 更新产量预测记录
     *
     * @param id         记录ID
     * @param prediction 更新信息
     * @return 更新后的记录
     */
    public YieldPrediction updateYieldPrediction(Long id, YieldPrediction prediction) {
        YieldPrediction existingPrediction = getYieldPredictionById(id);

        if (prediction.getPredictYear() != null) {
            existingPrediction.setPredictYear(prediction.getPredictYear());
        }
        if (prediction.getPredictYield() != null) {
            existingPrediction.setPredictYield(prediction.getPredictYield());
        }
        if (prediction.getActualYield() != null) {
            existingPrediction.setActualYield(prediction.getActualYield());
        }
        if (prediction.getPredictModel() != null) {
            existingPrediction.setPredictModel(prediction.getPredictModel());
        }
        if (prediction.getConfidence() != null) {
            existingPrediction.setConfidence(prediction.getConfidence());
        }
        if (prediction.getFactors() != null) {
            existingPrediction.setFactors(prediction.getFactors());
        }

        return yieldPredictionRepository.save(existingPrediction);
    }

    /**
     * 删除产量预测记录（逻辑删除）
     *
     * @param id 记录ID
     */
    public void deleteYieldPrediction(Long id) {
        YieldPrediction prediction = getYieldPredictionById(id);
        prediction.setDeleted(1);
        yieldPredictionRepository.save(prediction);
    }
}