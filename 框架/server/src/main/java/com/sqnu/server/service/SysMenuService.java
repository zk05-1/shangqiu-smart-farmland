package com.sqnu.server.service;

import com.sqnu.server.entity.SysMenu;
import com.sqnu.server.repository.SysMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuService {

    @Autowired
    private SysMenuRepository menuRepository;

    public List<SysMenu> getAllMenus() {
        return menuRepository.findAllByOrderBySortOrderAsc();
    }

    public List<SysMenu> getTopLevelMenus() {
        return menuRepository.findByParentIdIsNull();
    }

    public List<SysMenu> getMenusByParentId(Long parentId) {
        return menuRepository.findByParentId(parentId);
    }

    public List<SysMenu> getEnabledMenus() {
        return menuRepository.findByStatus(1);
    }

    public SysMenu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    public SysMenu createMenu(SysMenu menu) {
        return menuRepository.save(menu);
    }

    public SysMenu updateMenu(Long id, SysMenu menu) {
        SysMenu existingMenu = menuRepository.findById(id).orElse(null);
        if (existingMenu != null) {
            existingMenu.setParentId(menu.getParentId());
            existingMenu.setMenuName(menu.getMenuName());
            existingMenu.setMenuType(menu.getMenuType());
            existingMenu.setPath(menu.getPath());
            existingMenu.setComponent(menu.getComponent());
            existingMenu.setIcon(menu.getIcon());
            existingMenu.setSortOrder(menu.getSortOrder());
            existingMenu.setPermission(menu.getPermission());
            existingMenu.setStatus(menu.getStatus());
            return menuRepository.save(existingMenu);
        }
        return null;
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public List<Map<String, Object>> getMenuTree() {
        List<SysMenu> allMenus = getAllMenus();
        Map<Long, Map<String, Object>> menuMap = new HashMap<>();
        List<Map<String, Object>> rootMenus = new ArrayList<>();

        for (SysMenu menu : allMenus) {
            Map<String, Object> menuNode = new HashMap<>();
            menuNode.put("id", menu.getId());
            menuNode.put("parentId", menu.getParentId());
            menuNode.put("menuName", menu.getMenuName());
            menuNode.put("menuType", menu.getMenuType());
            menuNode.put("path", menu.getPath());
            menuNode.put("icon", menu.getIcon());
            menuNode.put("sortOrder", menu.getSortOrder());
            menuNode.put("status", menu.getStatus());
            menuNode.put("children", new ArrayList<>());
            menuMap.put(menu.getId(), menuNode);
        }

        for (SysMenu menu : allMenus) {
            Map<String, Object> menuNode = menuMap.get(menu.getId());
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                rootMenus.add(menuNode);
            } else {
                Map<String, Object> parentNode = menuMap.get(menu.getParentId());
                if (parentNode != null) {
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parentNode.get("children");
                    children.add(menuNode);
                }
            }
        }

        return rootMenus;
    }
}