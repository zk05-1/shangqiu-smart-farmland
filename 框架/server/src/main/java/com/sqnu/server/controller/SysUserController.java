package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.SysUser;
import com.sqnu.server.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @GetMapping("/list")
    public CommonResult<PageResult<SysUser>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<SysUser> result = userService.getUserList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public CommonResult<List<SysUser>> getAllUsers() {
        try {
            List<SysUser> users = userService.getAllUsers();
            return CommonResult.success(users);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CommonResult<SysUser> getUserById(@PathVariable Long id) {
        try {
            SysUser user = userService.getUserById(id);
            return CommonResult.success(user);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    @PostMapping
    public CommonResult<SysUser> createUser(@RequestBody SysUser user) {
        try {
            if (userService.existsByUsername(user.getUsername())) {
                return CommonResult.error("用户名已存在");
            }
            SysUser createdUser = userService.createUser(user);
            return CommonResult.success("创建成功", createdUser);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public CommonResult<SysUser> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        try {
            SysUser updatedUser = userService.updateUser(id, user);
            if (updatedUser != null) {
                return CommonResult.success("更新成功", updatedUser);
            }
            return CommonResult.error("用户不存在");
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/password")
    public CommonResult<Void> updatePassword(@PathVariable Long id, @RequestBody Map<String, String> params) {
        try {
            String newPassword = params.get("password");
            if (newPassword == null || newPassword.isEmpty()) {
                return CommonResult.error("密码不能为空");
            }
            userService.updatePassword(id, newPassword);
            return CommonResult.success("密码更新成功");
        } catch (Exception e) {
            return CommonResult.error("密码更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}