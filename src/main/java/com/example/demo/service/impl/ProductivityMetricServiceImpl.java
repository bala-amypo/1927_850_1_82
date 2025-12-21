package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ProductivityMetricRecord;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.ProductivityMetricRecordRepository;
import com.example.demo.service.ProductivityMetricService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductivityMetricServiceImpl implements ProductivityMetricService {

    private final ProductivityMetricRecordRepository metricRepository;
    private final EmployeeProfileRepository employeeRepository;

    public ProductivityMetricServiceImpl(ProductivityMetricRecordRepository metricRepository,
                                         EmployeeProfileRepository employeeRepository) {
        this.metricRepository = metricRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ProductivityMetricRecord recordMetric(ProductivityMetricRecord metric) {
        if (metric.getEmployeeId() == null || !employeeRepository.existsById(metric.getEmployeeId())) {
            throw new ResourceNotFoundException("Employee not found");
        }
        
        double rawScore = (metric.getHoursLogged() * 10) + 
                          (metric.getTasksCompleted() * 5) + 
                          (metric.getMeetingsAttended() * 2);
        double finalScore = Math.max(0, Math.min(100, rawScore));
        metric.setProductivityScore(finalScore);

        try {
            return metricRepository.save(metric);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Metric already exists for this employee and date");
        }
    }

    @Override
    public List<ProductivityMetricRecord> getMetricsByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found");
        }
        return metricRepository.findByEmployeeId(employeeId);
    }

    @Override
    public ProductivityMetricRecord getMetricById(Long id) {
        return metricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Metric not found"));
    }
}