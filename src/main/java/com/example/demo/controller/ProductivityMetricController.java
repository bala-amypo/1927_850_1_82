package com.example.demo.controller;

import com.example.demo.dto.ProductivityMetricDto;
import com.example.demo.model.ProductivityMetricRecord;
import com.example.demo.service.ProductivityMetricService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class ProductivityMetricController {

    private final ProductivityMetricService metricService;

    public ProductivityMetricController(ProductivityMetricService metricService) {
        this.metricService = metricService;
    }

    @PostMapping
    public ResponseEntity<ProductivityMetricRecord> recordMetric(@RequestBody ProductivityMetricDto dto) {
        ProductivityMetricRecord entity = new ProductivityMetricRecord();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setDate(dto.getDate());
        entity.setHoursLogged(dto.getHoursLogged());
        entity.setTasksCompleted(dto.getTasksCompleted());
        entity.setMeetingsAttended(dto.getMeetingsAttended());
        entity.setRawDataJson(dto.getRawDataJson());
        
        return new ResponseEntity<>(metricService.recordMetric(entity), HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ProductivityMetricRecord>> getMetricsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(metricService.getMetricsByEmployee(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductivityMetricRecord> getMetricById(@PathVariable Long id) {
        return ResponseEntity.ok(metricService.getMetricById(id));
    }
}