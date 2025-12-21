package com.example.demo.repository;

import com.example.demo.model.AnomalyFlagRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnomalyFlagRecordRepository extends JpaRepository<AnomalyFlagRecord, Long> {
    List<AnomalyFlagRecord> findByMetricId(Long metricId);
    List<AnomalyFlagRecord> findByEmployeeId(Long employeeId);
}