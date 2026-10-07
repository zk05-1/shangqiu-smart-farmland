package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.SysRole;
import com.sqnu.server.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SysRoleService {

    @Autowired
    private SysRoleRepository roleRepository;

    public PageResult<SysRole> getRoleList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        Page<SysRole> pageResult = roleRepository.findAll(pageable);
        return new PageResult<>(pageResult.getContent(), pageResult.getTotalElements(), page, size);
    }

    public List<SysRole> getAllRoles() {
        return roleRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"));
    }

    public SysRole getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Optional<SysRole> getRoleByCode(String roleCode) {
        return roleRepository.findByRoleCode(roleCode);
    }

    public SysRole createRole(SysRole role) {
        return roleRepository.save(role);
    }

    public SysRole updateRole(Long id, SysRole role) {
        SysRole existingRole = roleRepository.findById(id).orElse(null);
        if (existingRole != null) {
            existingRole.setRoleName(role.getRoleName());
            existingRole.setRoleCode(role.getRoleCode());
            existingRole.setDescription(role.getDescription());
            existingRole.setSortOrder(role.getSortOrder());
            existingRole.setStatus(role.getStatus());
            return roleRepository.save(existingRole);
        }
        return null;
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public boolean existsByRoleCode(String roleCode) {
        return roleRepository.findByRoleCode(roleCode).isPresent();
    }

    public boolean existsByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName).isPresent();
    }
}