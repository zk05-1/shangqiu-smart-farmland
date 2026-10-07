package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.FertilizationRecord;
import com.sqnu.server.service.FertilizationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 施肥记录控制器
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/fertilization")
public class FertilizationRecordController {

    @Autowired
    private FertilizationRecordService fertilizationRecordService;

    /**
     * 分页查询施肥记录列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public CommonResult<PageResult<FertilizationRecord>> getFertilizationRecordList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<FertilizationRecord> result = fertilizationRecordService.getFertilizationRecordList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取施肥记录详情
     *
     * @param id 记录ID
     * @return 施肥记录详情
     */
    @GetMapping("/{id}")
    public CommonResult<FertilizationRecord> getFertilizationRecordById(@PathVariable Long id) {
        try {
            FertilizationRecord record = fertilizationRecordService.getFertilizationRecordById(id);
            return CommonResult.success(record);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据农田ID查询施肥记录
     *
     * @param farmlandId 农田ID
     * @return 施肥记录列表
     */
    @GetMapping("/farmland/{farmlandId}")
    public CommonResult<List<FertilizationRecord>> getFertilizationRecordsByFarmlandId(@PathVariable Long farmlandId) {
        try {
            List<FertilizationRecord> records = fertilizationRecordService.getFertilizationRecordsByFarmlandId(farmlandId);
            return CommonResult.success(records);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建施肥记录
     *
     * @param record 施肥记录信息
     * @return 创建结果
     */
    @PostMapping
    public CommonResult<FertilizationRecord> createFertilizationRecord(@RequestBody FertilizationRecord record) {
        try {
            FertilizationRecord createdRecord = fertilizationRecordService.createFertilizationRecord(record);
            return CommonResult.success("创建成功", createdRecord);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新施肥记录
     *
     * @param id     记录ID
     * @param record 更新信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public CommonResult<FertilizationRecord> updateFertilizationRecord(@PathVariable Long id, @RequestBody FertilizationRecord record) {
        try {
            FertilizationRecord updatedRecord = fertilizationRecordService.updateFertilizationRecord(id, record);
            return CommonResult.success("更新成功", updatedRecord);
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除施肥记录（逻辑删除）
     *
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteFertilizationRecord(@PathVariable Long id) {
        try {
            fertilizationRecordService.deleteFertilizationRecord(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}