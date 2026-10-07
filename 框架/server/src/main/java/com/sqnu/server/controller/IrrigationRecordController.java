package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.IrrigationRecord;
import com.sqnu.server.service.IrrigationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 灌溉记录控制器
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/irrigation")
public class IrrigationRecordController {

    @Autowired
    private IrrigationRecordService irrigationRecordService;

    /**
     * 分页查询灌溉记录列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public CommonResult<PageResult<IrrigationRecord>> getIrrigationRecordList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<IrrigationRecord> result = irrigationRecordService.getIrrigationRecordList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取灌溉记录详情
     *
     * @param id 记录ID
     * @return 灌溉记录详情
     */
    @GetMapping("/{id}")
    public CommonResult<IrrigationRecord> getIrrigationRecordById(@PathVariable Long id) {
        try {
            IrrigationRecord record = irrigationRecordService.getIrrigationRecordById(id);
            return CommonResult.success(record);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据农田ID查询灌溉记录
     *
     * @param farmlandId 农田ID
     * @return 灌溉记录列表
     */
    @GetMapping("/farmland/{farmlandId}")
    public CommonResult<List<IrrigationRecord>> getIrrigationRecordsByFarmlandId(@PathVariable Long farmlandId) {
        try {
            List<IrrigationRecord> records = irrigationRecordService.getIrrigationRecordsByFarmlandId(farmlandId);
            return CommonResult.success(records);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建灌溉记录
     *
     * @param record 灌溉记录信息
     * @return 创建结果
     */
    @PostMapping
    public CommonResult<IrrigationRecord> createIrrigationRecord(@RequestBody IrrigationRecord record) {
        try {
            IrrigationRecord createdRecord = irrigationRecordService.createIrrigationRecord(record);
            return CommonResult.success("创建成功", createdRecord);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新灌溉记录
     *
     * @param id     记录ID
     * @param record 更新信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public CommonResult<IrrigationRecord> updateIrrigationRecord(@PathVariable Long id, @RequestBody IrrigationRecord record) {
        try {
            IrrigationRecord updatedRecord = irrigationRecordService.updateIrrigationRecord(id, record);
            return CommonResult.success("更新成功", updatedRecord);
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除灌溉记录（逻辑删除）
     *
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteIrrigationRecord(@PathVariable Long id) {
        try {
            irrigationRecordService.deleteIrrigationRecord(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}