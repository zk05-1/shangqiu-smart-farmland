package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.SysRole;
import com.sqnu.server.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/role")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;

    @GetMapping("/list")
    public CommonResult<PageResult<SysRole>> getRoleList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<SysRole> result = roleService.getRoleList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public CommonResult<List<SysRole>> getAllRoles() {
        try {
            List<SysRole> roles = roleService.getAllRoles();
            return CommonResult.success(roles);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CommonResult<SysRole> getRoleById(@PathVariable Long id) {
        try {
            SysRole role = roleService.getRoleById(id);
            return CommonResult.success(role);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    @PostMapping
    public CommonResult<SysRole> createRole(@RequestBody SysRole role) {
        try {
            if (roleService.existsByRoleCode(role.getRoleCode())) {
                return CommonResult.error("角色编码已存在");
            }
            if (roleService.existsByRoleName(role.getRoleName())) {
                return CommonResult.error("角色名称已存在");
            }
            SysRole createdRole = roleService.createRole(role);
            return CommonResult.success("创建成功", createdRole);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public CommonResult<SysRole> updateRole(@PathVariable Long id, @RequestBody SysRole role) {
        try {
            SysRole updatedRole = roleService.updateRole(id, role);
            if (updatedRole != null) {
                return CommonResult.success("更新成功", updatedRole);
            }
            return CommonResult.error("角色不存在");
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}