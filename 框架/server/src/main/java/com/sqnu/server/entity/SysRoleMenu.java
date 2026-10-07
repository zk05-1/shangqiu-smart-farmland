package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "sys_role_menu", uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "menu_id"}))
public class SysRoleMenu extends BaseEntity {

    private Long roleId;
    private Long menuId;

    @Column(name = "role_id", nullable = false)
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Column(name = "menu_id", nullable = false)
    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}