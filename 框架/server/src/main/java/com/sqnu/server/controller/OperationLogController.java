package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.OperationLog;
import com.sqnu.server.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/log")
public class OperationLogController {

    @Autowired
    private OperationLogService logService;

    @GetMapping("/list")
    public CommonResult<PageResult<OperationLog>> getLogList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<OperationLog> result = logService.getLogList(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/recent")
    public CommonResult<List<OperationLog>> getRecentLogs() {
        try {
            List<OperationLog> logs = logService.getRecentLogs();
            return CommonResult.success(logs);
        } catch (Exception e) {
            return CommonResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CommonResult<OperationLog> getLogById(@PathVariable Long id) {
        try {
            OperationLog log = logService.getLogById(id);
            return CommonResult.success(log);
        } catch (Exception e) {
            return CommonResult.error("获取详情失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteLog(@PathVariable Long id) {
        try {
            logService.deleteLog(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public CommonResult<Void> clearAllLogs() {
        try {
            logService.clearAllLogs();
            return CommonResult.success("清空成功");
        } catch (Exception e) {
            return CommonResult.error("清空失败: " + e.getMessage());
        }
    }
}