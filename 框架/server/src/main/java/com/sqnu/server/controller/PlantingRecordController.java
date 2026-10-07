package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.PlantingRecord;
import com.sqnu.server.service.PlantingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 种植记录控制器
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/planting")
public class PlantingRecordController {

    @Autowired
    private PlantingRecordService plantingRecordService;

    /**
     * 分页查询种植记录列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public CommonResult<PageResult<PlantingRecord>> getPlantingRecordList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<PlantingRecord> result = plantingRecordService.getPlantingRecordList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取种植记录详情
     *
     * @param id 记录ID
     * @return 种植记录详情
     */
    @GetMapping("/{id}")
    public CommonResult<PlantingRecord> getPlantingRecordById(@PathVariable Long id) {
        try {
            PlantingRecord record = plantingRecordService.getPlantingRecordById(id);
            return CommonResult.success(record);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据农田ID查询种植记录
     *
     * @param farmlandId 农田ID
     * @return 种植记录列表
     */
    @GetMapping("/farmland/{farmlandId}")
    public CommonResult<List<PlantingRecord>> getPlantingRecordsByFarmlandId(@PathVariable Long farmlandId) {
        try {
            List<PlantingRecord> records = plantingRecordService.getPlantingRecordsByFarmlandId(farmlandId);
            return CommonResult.success(records);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建种植记录
     *
     * @param record 种植记录信息
     * @return 创建结果
     */
    @PostMapping
    public CommonResult<PlantingRecord> createPlantingRecord(@RequestBody PlantingRecord record) {
        try {
            PlantingRecord createdRecord = plantingRecordService.createPlantingRecord(record);
            return CommonResult.success("创建成功", createdRecord);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新种植记录
     *
     * @param id     记录ID
     * @param record 更新信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public CommonResult<PlantingRecord> updatePlantingRecord(@PathVariable Long id, @RequestBody PlantingRecord record) {
        try {
            PlantingRecord updatedRecord = plantingRecordService.updatePlantingRecord(id, record);
            return CommonResult.success("更新成功", updatedRecord);
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除种植记录（逻辑删除）
     *
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> deletePlantingRecord(@PathVariable Long id) {
        try {
            plantingRecordService.deletePlantingRecord(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}