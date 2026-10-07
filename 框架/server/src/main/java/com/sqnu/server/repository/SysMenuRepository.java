package com.sqnu.server.repository;

import com.sqnu.server.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, Long> {

    List<SysMenu> findByParentId(Long parentId);

    List<SysMenu> findByParentIdIsNull();

    List<SysMenu> findByStatus(Integer status);

    List<SysMenu> findAllByOrderBySortOrderAsc();
}