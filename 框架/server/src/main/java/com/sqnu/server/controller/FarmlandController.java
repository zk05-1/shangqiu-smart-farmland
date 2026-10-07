package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.Farmland;
import com.sqnu.server.service.FarmlandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 农田地块控制器
 * <p>
 * 提供农田地块的增删改查接口。
 * </p>
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/farmland")
public class FarmlandController {

    @Autowired
    private FarmlandService farmlandService;

    /**
     * 分页查询农田地块列表
     *
     * @param page   页码（默认第1页）
     * @param size   每页大小（默认10条）
     * @param keyword 搜索关键字
     * @return 分页结果
     */
    @GetMapping("/list")
    public CommonResult<PageResult<Farmland>> getFarmlandList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            PageResult<Farmland> result = farmlandService.getFarmlandList(page, size, keyword);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取农田地块详情
     *
     * @param id 地块ID
     * @return 地块详情
     */
    @GetMapping("/{id}")
    public CommonResult<Farmland> getFarmlandById(@PathVariable Long id) {
        try {
            Farmland farmland = farmlandService.getFarmlandById(id);
            return CommonResult.success(farmland);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建农田地块
     *
     * @param farmland 地块信息
     * @return 创建结果
     */
    @PostMapping
    public CommonResult<Farmland> createFarmland(@RequestBody Farmland farmland) {
        try {
            Farmland createdFarmland = farmlandService.createFarmland(farmland);
            return CommonResult.success("创建成功", createdFarmland);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新农田地块
     *
     * @param id       地块ID
     * @param farmland 更新信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public CommonResult<Farmland> updateFarmland(@PathVariable Long id, @RequestBody Farmland farmland) {
        try {
            Farmland updatedFarmland = farmlandService.updateFarmland(id, farmland);
            return CommonResult.success("更新成功", updatedFarmland);
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除农田地块（逻辑删除）
     *
     * @param id 地块ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteFarmland(@PathVariable Long id) {
        try {
            farmlandService.deleteFarmland(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据状态查询地块列表
     *
     * @param status 状态
     * @return 地块列表
     */
    @GetMapping("/status/{status}")
    public CommonResult<List<Farmland>> getFarmlandByStatus(@PathVariable String status) {
        try {
            List<Farmland> farmlands = farmlandService.getFarmlandByStatus(status);
            return CommonResult.success(farmlands);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 统计地块总数
     *
     * @return 地块总数
     */
    @GetMapping("/count")
    public CommonResult<Long> countFarmland() {
        try {
            long count = farmlandService.countFarmland();
            return CommonResult.success(count);
        } catch (Exception e) {
            return CommonResult.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 统计使用中的地块数量
     *
     * @return 使用中的地块数量
     */
    @GetMapping("/count/active")
    public CommonResult<Long> countActiveFarmland() {
        try {
            long count = farmlandService.countActiveFarmland();
            return CommonResult.success(count);
        } catch (Exception e) {
            return CommonResult.error("统计失败: " + e.getMessage());
        }
    }
}