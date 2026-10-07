package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.OperationLog;
import com.sqnu.server.repository.OperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogService {

    @Autowired
    private OperationLogRepository logRepository;

    public PageResult<OperationLog> getLogList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<OperationLog> pageResult = logRepository.findAll(pageable);
        return new PageResult<>(pageResult.getContent(), pageResult.getTotalElements(), page, size);
    }

    public List<OperationLog> getRecentLogs() {
        return logRepository.findTop50ByOrderByCreatedTimeDesc();
    }

    public OperationLog getLogById(Long id) {
        return logRepository.findById(id).orElse(null);
    }

    public OperationLog createLog(OperationLog log) {
        return logRepository.save(log);
    }

    public void deleteLog(Long id) {
        logRepository.deleteById(id);
    }

    public void clearAllLogs() {
        logRepository.deleteAll();
    }
}