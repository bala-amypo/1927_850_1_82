package com.example.demo.service;

import com.example.demo.model.ProductivityMetricRecord;
import java.util.List;

public interface ProductivityMetricService {
    ProductivityMetricRecord recordMetric(ProductivityMetricRecord metric);
    List<ProductivityMetricRecord> getMetricsByEmployee(Long employeeId);
    ProductivityMetricRecord getMetricById(Long id);
}