package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ProductivityMetricRecord;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.ProductivityMetricRecordRepository;
import com.example.demo.service.ProductivityMetricService;
import com.example.demo.util.ProductivityCalculator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductivityMetricServiceImpl implements ProductivityMetricService {

    private final ProductivityMetricRecordRepository metricRepository;
    private final EmployeeProfileRepository employeeRepository;
    private final ProductivityCalculator calculator;

    public ProductivityMetricServiceImpl(ProductivityMetricRecordRepository metricRepository,
                                         EmployeeProfileRepository employeeRepository,
                                         ProductivityCalculator calculator) {
        this.metricRepository = metricRepository;
        this.employeeRepository = employeeRepository;
        this.calculator = calculator;
    }

    @Override
    public ProductivityMetricRecord recordMetric(ProductivityMetricRecord metric) {
        if (metric.getEmployeeId() == null || !employeeRepository.existsById(metric.getEmployeeId())) {
            throw new ResourceNotFoundException("Employee not found");
        }
        
        double score = calculator.calculateProductivityScore(
            metric.getHoursLogged(), 
            metric.getTasksCompleted(), 
            metric.getMeetingsAttended()
        );
        metric.setProductivityScore(score);

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
    public Optional<ProductivityMetricRecord> getMetricById(Long id) {
        return metricRepository.findById(id);
    }

    @Override
    public List<ProductivityMetricRecord> getAllMetrics() {
        return metricRepository.findAll();
    }

    @Override
    public ProductivityMetricRecord updateMetric(Long id, ProductivityMetricRecord updated) {
        Optional<ProductivityMetricRecord> existingOpt = getMetricById(id);
        if (existingOpt.isEmpty()) {
            throw new ResourceNotFoundException("Metric not found");
        }
        ProductivityMetricRecord existing = existingOpt.get();
        
        existing.setHoursLogged(updated.getHoursLogged());
        existing.setTasksCompleted(updated.getTasksCompleted());
        existing.setMeetingsAttended(updated.getMeetingsAttended());
        existing.setRawDataJson(updated.getRawDataJson());
        
        double score = calculator.calculateProductivityScore(
            existing.getHoursLogged(), 
            existing.getTasksCompleted(), 
            existing.getMeetingsAttended()
        );
        existing.setProductivityScore(score);
        
        return metricRepository.save(existing);
    }
}