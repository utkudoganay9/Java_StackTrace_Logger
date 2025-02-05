package org.example.repository;

import org.example.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Integer> {
}
