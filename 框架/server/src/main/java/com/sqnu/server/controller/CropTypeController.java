package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.entity.CropType;
import com.sqnu.server.service.CropTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作物类型控制器
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/crop-type")
public class CropTypeController {

    @Autowired
    private CropTypeService cropTypeService;

    /**
     * 查询所有作物类型列表
     *
     * @return 作物类型列表
     */
    @GetMapping("/list")
    public CommonResult<List<CropType>> getAllCropTypes() {
        try {
            List<CropType> cropTypes = cropTypeService.getAllCropTypes();
            return CommonResult.success(cropTypes);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取作物类型详情
     *
     * @param id 作物类型ID
     * @return 作物类型详情
     */
    @GetMapping("/{id}")
    public CommonResult<CropType> getCropTypeById(@PathVariable Long id) {
        try {
            CropType cropType = cropTypeService.getCropTypeById(id);
            return CommonResult.success(cropType);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建作物类型
     *
     * @param cropType 作物类型信息
     * @return 创建结果
     */
    @PostMapping
    public CommonResult<CropType> createCropType(@RequestBody CropType cropType) {
        try {
            CropType createdCropType = cropTypeService.createCropType(cropType);
            return CommonResult.success("创建成功", createdCropType);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新作物类型
     *
     * @param id       作物类型ID
     * @param cropType 更新信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public CommonResult<CropType> updateCropType(@PathVariable Long id, @RequestBody CropType cropType) {
        try {
            CropType updatedCropType = cropTypeService.updateCropType(id, cropType);
            return CommonResult.success("更新成功", updatedCropType);
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除作物类型（逻辑删除）
     *
     * @param id 作物类型ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteCropType(@PathVariable Long id) {
        try {
            cropTypeService.deleteCropType(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}