package com.example.demo.repository;

import com.example.demo.model.ProductivityMetricRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface ProductivityMetricRecordRepository extends JpaRepository<ProductivityMetricRecord, Long> {
    List<ProductivityMetricRecord> findByEmployeeId(Long employeeId);
    Optional<ProductivityMetricRecord> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
}