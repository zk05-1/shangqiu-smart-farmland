package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.Farmland;
import com.sqnu.server.repository.FarmlandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 农田地块服务层
 * <p>
 * 提供农田地块的增删改查业务逻辑处理。
 * </p>
 *
 * @author sqnu
 */
@Service
@Transactional
public class FarmlandService {

    @Autowired
    private FarmlandRepository farmlandRepository;

    /**
     * 分页查询农田地块
     *
     * @param page     页码
     * @param size     每页大小
     * @param keyword  搜索关键字
     * @return 分页结果
     */
    public PageResult<Farmland> getFarmlandList(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<Farmland> farmlandPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            farmlandPage = farmlandRepository.searchByKeyword(keyword, pageable);
        } else {
            farmlandPage = farmlandRepository.findByDeletedFalse(pageable);
        }

        return new PageResult<>(
                farmlandPage.getContent(),
                farmlandPage.getTotalElements(),
                farmlandPage.getNumber() + 1,
                farmlandPage.getSize()
        );
    }

    /**
     * 根据ID获取农田地块详情
     *
     * @param id 地块ID
     * @return 地块详情
     */
    public Farmland getFarmlandById(Long id) {
        return farmlandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("农田地块不存在"));
    }

    /**
     * 创建农田地块
     *
     * @param farmland 地块信息
     * @return 创建后的地块
     */
    public Farmland createFarmland(Farmland farmland) {
        // 检查地块编号是否已存在
        if (farmlandRepository.findByFarmlandCode(farmland.getFarmlandCode()) != null) {
            throw new RuntimeException("地块编号已存在");
        }
        return farmlandRepository.save(farmland);
    }

    /**
     * 更新农田地块
     *
     * @param id       地块ID
     * @param farmland 更新信息
     * @return 更新后的地块
     */
    public Farmland updateFarmland(Long id, Farmland farmland) {
        Farmland existingFarmland = getFarmlandById(id);

        // 更新字段
        if (farmland.getFarmlandName() != null) {
            existingFarmland.setFarmlandName(farmland.getFarmlandName());
        }
        if (farmland.getArea() != null) {
            existingFarmland.setArea(farmland.getArea());
        }
        if (farmland.getLocation() != null) {
            existingFarmland.setLocation(farmland.getLocation());
        }
        if (farmland.getLongitude() != null) {
            existingFarmland.setLongitude(farmland.getLongitude());
        }
        if (farmland.getLatitude() != null) {
            existingFarmland.setLatitude(farmland.getLatitude());
        }
        if (farmland.getSoilType() != null) {
            existingFarmland.setSoilType(farmland.getSoilType());
        }
        if (farmland.getSoilPh() != null) {
            existingFarmland.setSoilPh(farmland.getSoilPh());
        }
        if (farmland.getOwnerName() != null) {
            existingFarmland.setOwnerName(farmland.getOwnerName());
        }
        if (farmland.getOwnerPhone() != null) {
            existingFarmland.setOwnerPhone(farmland.getOwnerPhone());
        }
        if (farmland.getStatus() != null) {
            existingFarmland.setStatus(farmland.getStatus());
        }
        if (farmland.getDescription() != null) {
            existingFarmland.setDescription(farmland.getDescription());
        }
        if (farmland.getImageUrl() != null) {
            existingFarmland.setImageUrl(farmland.getImageUrl());
        }

        return farmlandRepository.save(existingFarmland);
    }

    /**
     * 删除农田地块（逻辑删除）
     *
     * @param id 地块ID
     */
    public void deleteFarmland(Long id) {
        Farmland farmland = getFarmlandById(id);
        farmland.setDeleted(1);
        farmlandRepository.save(farmland);
    }

    /**
     * 根据状态查询地块列表
     *
     * @param status 状态
     * @return 地块列表
     */
    public List<Farmland> getFarmlandByStatus(String status) {
        return farmlandRepository.findByStatus(status);
    }

    /**
     * 统计地块总数
     *
     * @return 地块总数
     */
    public long countFarmland() {
        return farmlandRepository.count();
    }

    /**
     * 统计使用中的地块数量
     *
     * @return 使用中的地块数量
     */
    public long countActiveFarmland() {
        return farmlandRepository.countByStatusAndDeletedFalse("USE");
    }
}