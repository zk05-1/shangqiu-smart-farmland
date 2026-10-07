package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.entity.SysUser;
import com.sqnu.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 * <p>
 * 提供用户登录、注册等接口。
 * </p>
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录接口
     *
     * @param loginRequest 登录请求（包含username和password）
     * @return 登录结果（包含token和用户信息）
     */
    @PostMapping("/login")
    public CommonResult<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            if (username == null || password == null) {
                return CommonResult.error("用户名和密码不能为空");
            }

            Map<String, Object> result = authService.login(username, password);
            return CommonResult.success("登录成功", result);
        } catch (Exception e) {
            return CommonResult.error("登录失败: " + e.getMessage());
        }
    }

    /**
     * 用户注册接口
     *
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public CommonResult<SysUser> register(@RequestBody SysUser user) {
        try {
            SysUser registeredUser = authService.register(user);
            return CommonResult.success("注册成功", registeredUser);
        } catch (Exception e) {
            return CommonResult.error("注册失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息接口
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/user/{username}")
    public CommonResult<SysUser> getUser(@PathVariable String username) {
        try {
            SysUser user = authService.getUserByUsername(username);
            return CommonResult.success(user);
        } catch (Exception e) {
            return CommonResult.error("获取用户信息失败: " + e.getMessage());
        }
    }
}