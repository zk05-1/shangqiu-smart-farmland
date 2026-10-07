package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.SensorData;
import com.sqnu.server.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensor")
public class SensorDataController {

    @Autowired
    private SensorDataService sensorDataService;

    @GetMapping("/list")
    public CommonResult<PageResult<SensorData>> getSensorDataList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<SensorData> result = sensorDataService.getSensorDataList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/farmland/{farmlandId}")
    public CommonResult<PageResult<SensorData>> getSensorDataByFarmlandId(
            @PathVariable Long farmlandId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<SensorData> result = sensorDataService.getSensorDataByFarmlandId(farmlandId, page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/recent/{farmlandId}")
    public CommonResult<List<SensorData>> getRecentSensorData(@PathVariable Long farmlandId) {
        try {
            List<SensorData> result = sensorDataService.getRecentSensorData(farmlandId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CommonResult<SensorData> getSensorDataById(@PathVariable Long id) {
        try {
            SensorData sensorData = sensorDataService.getSensorDataById(id);
            return CommonResult.success(sensorData);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    @PostMapping
    public CommonResult<SensorData> createSensorData(@RequestBody SensorData sensorData) {
        try {
            SensorData createdData = sensorDataService.createSensorData(sensorData);
            return CommonResult.success("创建成功", createdData);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public CommonResult<SensorData> updateSensorData(@PathVariable Long id, @RequestBody SensorData sensorData) {
        try {
            SensorData updatedData = sensorDataService.updateSensorData(id, sensorData);
            return CommonResult.success("更新成功", updatedData);
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteSensorData(@PathVariable Long id) {
        try {
            sensorDataService.deleteSensorData(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}