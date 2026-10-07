package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.PestControl;
import com.sqnu.server.service.PestControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 病虫害防治控制器
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/pest-control")
public class PestControlController {

    @Autowired
    private PestControlService pestControlService;

    /**
     * 分页查询病虫害防治记录列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public CommonResult<PageResult<PestControl>> getPestControlList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<PestControl> result = pestControlService.getPestControlList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取病虫害防治记录详情
     *
     * @param id 记录ID
     * @return 病虫害防治记录详情
     */
    @GetMapping("/{id}")
    public CommonResult<PestControl> getPestControlById(@PathVariable Long id) {
        try {
            PestControl record = pestControlService.getPestControlById(id);
            return CommonResult.success(record);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据病虫害监测ID查询防治记录
     *
     * @param pestMonitorId 病虫害监测ID
     * @return 病虫害防治记录列表
     */
    @GetMapping("/pest-monitor/{pestMonitorId}")
    public CommonResult<List<PestControl>> getPestControlsByPestMonitorId(@PathVariable Long pestMonitorId) {
        try {
            List<PestControl> records = pestControlService.getPestControlsByPestMonitorId(pestMonitorId);
            return CommonResult.success(records);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建病虫害防治记录
     *
     * @param record 病虫害防治记录信息
     * @return 创建结果
     */
    @PostMapping
    public CommonResult<PestControl> createPestControl(@RequestBody PestControl record) {
        try {
            PestControl createdRecord = pestControlService.createPestControl(record);
            return CommonResult.success("创建成功", createdRecord);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新病虫害防治记录
     *
     * @param id     记录ID
     * @param record 更新信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public CommonResult<PestControl> updatePestControl(@PathVariable Long id, @RequestBody PestControl record) {
        try {
            PestControl updatedRecord = pestControlService.updatePestControl(id, record);
            return CommonResult.success("更新成功", updatedRecord);
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除病虫害防治记录（逻辑删除）
     *
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> deletePestControl(@PathVariable Long id) {
        try {
            pestControlService.deletePestControl(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}