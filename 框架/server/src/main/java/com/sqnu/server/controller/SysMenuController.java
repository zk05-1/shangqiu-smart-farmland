package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.entity.SysMenu;
import com.sqnu.server.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService menuService;

    @GetMapping("/tree")
    public CommonResult<List<Map<String, Object>>> getMenuTree() {
        try {
            List<Map<String, Object>> tree = menuService.getMenuTree();
            return CommonResult.success(tree);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public CommonResult<List<SysMenu>> getAllMenus() {
        try {
            List<SysMenu> menus = menuService.getAllMenus();
            return CommonResult.success(menus);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CommonResult<SysMenu> getMenuById(@PathVariable Long id) {
        try {
            SysMenu menu = menuService.getMenuById(id);
            return CommonResult.success(menu);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    @PostMapping
    public CommonResult<SysMenu> createMenu(@RequestBody SysMenu menu) {
        try {
            SysMenu createdMenu = menuService.createMenu(menu);
            return CommonResult.success("创建成功", createdMenu);
        } catch (Exception e) {
            return CommonResult.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public CommonResult<SysMenu> updateMenu(@PathVariable Long id, @RequestBody SysMenu menu) {
        try {
            SysMenu updatedMenu = menuService.updateMenu(id, menu);
            if (updatedMenu != null) {
                return CommonResult.success("更新成功", updatedMenu);
            }
            return CommonResult.error("菜单不存在");
        } catch (Exception e) {
            return CommonResult.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteMenu(@PathVariable Long id) {
        try {
            menuService.deleteMenu(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}