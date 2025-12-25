package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.AnomalyFlagRecord;
import com.example.demo.repository.AnomalyFlagRecordRepository;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.service.AnomalyFlagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class AnomalyFlagServiceImpl implements AnomalyFlagService {

    private final AnomalyFlagRecordRepository flagRepository;
    private final EmployeeProfileRepository employeeRepository;

    public AnomalyFlagServiceImpl(AnomalyFlagRecordRepository flagRepository, EmployeeProfileRepository employeeRepository) {
        this.flagRepository = flagRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public AnomalyFlagRecord flagAnomaly(AnomalyFlagRecord flag) {
        flag.setResolved(false);
        return flagRepository.save(flag);
    }

    @Override
    public List<AnomalyFlagRecord> getFlagsByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found");
        }
        return flagRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<AnomalyFlagRecord> getFlagsByMetric(Long metricId) {
        return flagRepository.findByMetricId(metricId);
    }

    @Override
    public AnomalyFlagRecord resolveFlag(Long flagId) {
        AnomalyFlagRecord flag = flagRepository.findById(flagId)
                .orElseThrow(() -> new ResourceNotFoundException("Flag not found"));
        flag.setResolved(true);
        return flagRepository.save(flag);
    }
}