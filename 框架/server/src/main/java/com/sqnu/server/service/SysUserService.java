package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.SysUser;
import com.sqnu.server.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SysUserService {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PageResult<SysUser> getUserList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<SysUser> pageResult = userRepository.findAll(pageable);
        return new PageResult<>(pageResult.getContent(), pageResult.getTotalElements(), page, size);
    }

    public List<SysUser> getAllUsers() {
        return userRepository.findAll();
    }

    public SysUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<SysUser> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public SysUser createUser(SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public SysUser updateUser(Long id, SysUser user) {
        SysUser existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setRealName(user.getRealName());
            existingUser.setPhone(user.getPhone());
            existingUser.setEmail(user.getEmail());
            existingUser.setGender(user.getGender());
            existingUser.setStatus(user.getStatus());
            if (user.getAvatar() != null) {
                existingUser.setAvatar(user.getAvatar());
            }
            return userRepository.save(existingUser);
        }
        return null;
    }

    public SysUser updatePassword(Long id, String newPassword) {
        SysUser user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}