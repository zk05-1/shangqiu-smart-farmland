package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.PestMonitor;
import com.sqnu.server.service.PestMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 病虫害监测控制器
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/pest-monitor")
public class PestMonitorController {

    @Autowired
    private PestMonitorService pestMonitorService;

    /**
     * 分页查询病虫害监测记录列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public CommonResult<PageResult<PestMonitor>> getPestMonitorList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<PestMonitor> result = pestMonitorService.getPestMonitorList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取病虫害监测记录详情
     *
     * @param id 记录ID
     * @return 病虫害监测记录详情
     */
    @GetMapping("/{id}")
    public CommonResult<PestMonitor> getPestMonitorById(@PathVariable Long id) {
        try {
            PestMonitor record = pestMonitorService.getPestMonitorById(id);
            return CommonResult.success(record);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据农田ID查询病虫害监测记录
     *
     * @param farmlandId 农田ID
     * @return 病虫害监测记录列表
     */
    @GetMapping("/farmland/{farmlandId}")
    public CommonResult<List<PestMonitor>> getPestMonitorsByFarmlandId(@PathVariable Long farmlandId) {
        try {
            List<PestMonitor> records = pestMonitorService.getPestMonitorsByFarmlandId(farmlandId);
            return CommonResult.success(records);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建病虫害监测记录
     *
     * @param record 病虫害监测记录信息
     * @return 创建结果
     */
    @PostMapping
    public CommonResult<PestMonitor> createPestMonitor(@RequestBody PestMonitor record) {
        try {
            PestMonitor createdRecord = pestMonitorService.createPestMonitor(record);
            return CommonResult.success("创建成功", createdRecord);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新病虫害监测记录
     *
     * @param id     记录ID
     * @param record 更新信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public CommonResult<PestMonitor> updatePestMonitor(@PathVariable Long id, @RequestBody PestMonitor record) {
        try {
            PestMonitor updatedRecord = pestMonitorService.updatePestMonitor(id, record);
            return CommonResult.success("更新成功", updatedRecord);
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除病虫害监测记录（逻辑删除）
     *
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> deletePestMonitor(@PathVariable Long id) {
        try {
            pestMonitorService.deletePestMonitor(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}