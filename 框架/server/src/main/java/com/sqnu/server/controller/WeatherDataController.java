package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.WeatherData;
import com.sqnu.server.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 气象数据控制器
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherDataController {

    @Autowired
    private WeatherDataService weatherDataService;

    /**
     * 分页查询气象数据列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public CommonResult<PageResult<WeatherData>> getWeatherDataList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<WeatherData> result = weatherDataService.getWeatherDataList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取气象数据详情
     *
     * @param id 气象数据ID
     * @return 气象数据详情
     */
    @GetMapping("/{id}")
    public CommonResult<WeatherData> getWeatherDataById(@PathVariable Long id) {
        try {
            WeatherData weatherData = weatherDataService.getWeatherDataById(id);
            return CommonResult.success(weatherData);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据农田ID查询气象数据
     *
     * @param farmlandId 农田ID
     * @return 气象数据列表
     */
    @GetMapping("/farmland/{farmlandId}")
    public CommonResult<List<WeatherData>> getWeatherDataByFarmlandId(@PathVariable Long farmlandId) {
        try {
            List<WeatherData> weatherData = weatherDataService.getWeatherDataByFarmlandId(farmlandId);
            return CommonResult.success(weatherData);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建气象数据
     *
     * @param weatherData 气象数据信息
     * @return 创建结果
     */
    @PostMapping
    public CommonResult<WeatherData> createWeatherData(@RequestBody WeatherData weatherData) {
        try {
            WeatherData createdData = weatherDataService.createWeatherData(weatherData);
            return CommonResult.success("创建成功", createdData);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新气象数据
     *
     * @param id          气象数据ID
     * @param weatherData 更新信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public CommonResult<WeatherData> updateWeatherData(@PathVariable Long id, @RequestBody WeatherData weatherData) {
        try {
            WeatherData updatedData = weatherDataService.updateWeatherData(id, weatherData);
            return CommonResult.success("更新成功", updatedData);
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除气象数据（逻辑删除）
     *
     * @param id 气象数据ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteWeatherData(@PathVariable Long id) {
        try {
            weatherDataService.deleteWeatherData(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}