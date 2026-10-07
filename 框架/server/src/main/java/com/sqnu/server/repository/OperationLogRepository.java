package com.sqnu.server.repository;

import com.sqnu.server.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    Page<OperationLog> findByUsername(String username, Pageable pageable);

    Page<OperationLog> findByOperation(String operation, Pageable pageable);

    List<OperationLog> findTop50ByOrderByCreatedTimeDesc();
}