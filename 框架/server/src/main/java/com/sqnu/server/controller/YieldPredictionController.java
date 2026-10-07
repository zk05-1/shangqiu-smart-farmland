package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.YieldPrediction;
import com.sqnu.server.service.YieldPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产量预测控制器
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/yield-prediction")
public class YieldPredictionController {

    @Autowired
    private YieldPredictionService yieldPredictionService;

    /**
     * 分页查询产量预测记录列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public CommonResult<PageResult<YieldPrediction>> getYieldPredictionList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<YieldPrediction> result = yieldPredictionService.getYieldPredictionList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取产量预测记录详情
     *
     * @param id 记录ID
     * @return 产量预测记录详情
     */
    @GetMapping("/{id}")
    public CommonResult<YieldPrediction> getYieldPredictionById(@PathVariable Long id) {
        try {
            YieldPrediction prediction = yieldPredictionService.getYieldPredictionById(id);
            return CommonResult.success(prediction);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据农田ID查询产量预测记录
     *
     * @param farmlandId 农田ID
     * @return 产量预测记录列表
     */
    @GetMapping("/farmland/{farmlandId}")
    public CommonResult<List<YieldPrediction>> getYieldPredictionsByFarmlandId(@PathVariable Long farmlandId) {
        try {
            List<YieldPrediction> predictions = yieldPredictionService.getYieldPredictionsByFarmlandId(farmlandId);
            return CommonResult.success(predictions);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建产量预测记录
     *
     * @param prediction 产量预测记录信息
     * @return 创建结果
     */
    @PostMapping
    public CommonResult<YieldPrediction> createYieldPrediction(@RequestBody YieldPrediction prediction) {
        try {
            YieldPrediction createdPrediction = yieldPredictionService.createYieldPrediction(prediction);
            return CommonResult.success("创建成功", createdPrediction);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新产量预测记录
     *
     * @param id         记录ID
     * @param prediction 更新信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public CommonResult<YieldPrediction> updateYieldPrediction(@PathVariable Long id, @RequestBody YieldPrediction prediction) {
        try {
            YieldPrediction updatedPrediction = yieldPredictionService.updateYieldPrediction(id, prediction);
            return CommonResult.success("更新成功", updatedPrediction);
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除产量预测记录（逻辑删除）
     *
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteYieldPrediction(@PathVariable Long id) {
        try {
            yieldPredictionService.deleteYieldPrediction(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}