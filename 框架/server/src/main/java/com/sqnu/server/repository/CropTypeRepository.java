package com.sqnu.server.repository;

import com.sqnu.server.entity.CropType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 作物类型数据访问层
 *
 * @author sqnu
 */
@Repository
public interface CropTypeRepository extends JpaRepository<CropType, Long> {

    /**
     * 根据作物编码查询
     *
     * @param cropCode 作物编码
     * @return 作物类型
     */
    Optional<CropType> findByCropCode(String cropCode);

    /**
     * 检查作物编码是否已存在
     *
     * @param cropCode 作物编码
     * @return 是否存在
     */
    boolean existsByCropCode(String cropCode);
}