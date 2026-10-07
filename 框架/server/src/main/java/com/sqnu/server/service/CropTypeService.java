package com.sqnu.server.service;

import com.sqnu.server.entity.CropType;
import com.sqnu.server.repository.CropTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作物类型服务层
 *
 * @author sqnu
 */
@Service
@Transactional
public class CropTypeService {

    @Autowired
    private CropTypeRepository cropTypeRepository;

    /**
     * 查询所有作物类型
     *
     * @return 作物类型列表
     */
    public List<CropType> getAllCropTypes() {
        return cropTypeRepository.findAll();
    }

    /**
     * 根据ID获取作物类型详情
     *
     * @param id 作物类型ID
     * @return 作物类型详情
     */
    public CropType getCropTypeById(Long id) {
        return cropTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("作物类型不存在"));
    }

    /**
     * 创建作物类型
     *
     * @param cropType 作物类型信息
     * @return 创建后的作物类型
     */
    public CropType createCropType(CropType cropType) {
        // 检查作物编码是否已存在
        if (cropTypeRepository.existsByCropCode(cropType.getCropCode())) {
            throw new RuntimeException("作物编码已存在");
        }
        return cropTypeRepository.save(cropType);
    }

    /**
     * 更新作物类型
     *
     * @param id       作物类型ID
     * @param cropType 更新信息
     * @return 更新后的作物类型
     */
    public CropType updateCropType(Long id, CropType cropType) {
        CropType existingCropType = getCropTypeById(id);

        if (cropType.getCropName() != null) {
            existingCropType.setCropName(cropType.getCropName());
        }
        if (cropType.getCategory() != null) {
            existingCropType.setCategory(cropType.getCategory());
        }
        if (cropType.getGrowthPeriod() != null) {
            existingCropType.setGrowthPeriod(cropType.getGrowthPeriod());
        }
        if (cropType.getSuitableSoil() != null) {
            existingCropType.setSuitableSoil(cropType.getSuitableSoil());
        }
        if (cropType.getSuitableTemp() != null) {
            existingCropType.setSuitableTemp(cropType.getSuitableTemp());
        }
        if (cropType.getDescription() != null) {
            existingCropType.setDescription(cropType.getDescription());
        }
        if (cropType.getImageUrl() != null) {
            existingCropType.setImageUrl(cropType.getImageUrl());
        }

        return cropTypeRepository.save(existingCropType);
    }

    /**
     * 删除作物类型（逻辑删除）
     *
     * @param id 作物类型ID
     */
    public void deleteCropType(Long id) {
        CropType cropType = getCropTypeById(id);
        cropType.setDeleted(1);
        cropTypeRepository.save(cropType);
    }
}