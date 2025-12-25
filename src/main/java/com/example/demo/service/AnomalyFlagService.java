package com.example.demo.service;

import com.example.demo.model.AnomalyFlagRecord;
import java.util.List;

public interface AnomalyFlagService {
    AnomalyFlagRecord flagAnomaly(AnomalyFlagRecord flag);
    List<AnomalyFlagRecord> getFlagsByEmployee(Long employeeId);
    List<AnomalyFlagRecord> getFlagsByMetric(Long metricId);
    AnomalyFlagRecord resolveFlag(Long flagId);
}