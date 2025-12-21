package com.example.demo.controller;

import com.example.demo.dto.AnomalyFlagDto;
import com.example.demo.model.AnomalyFlagRecord;
import com.example.demo.service.AnomalyFlagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/anomalies")
public class AnomalyFlagController {

    private final AnomalyFlagService flagService;

    public AnomalyFlagController(AnomalyFlagService flagService) {
        this.flagService = flagService;
    }

    @PostMapping
    public ResponseEntity<AnomalyFlagRecord> createFlag(@RequestBody AnomalyFlagDto dto) {
        AnomalyFlagRecord entity = new AnomalyFlagRecord();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setMetricId(dto.getMetricId());
        entity.setRuleCode(dto.getRuleCode());
        entity.setSeverity(dto.getSeverity());
        entity.setDetails(dto.getDetails());
        
        return new ResponseEntity<>(flagService.flagAnomaly(entity), HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AnomalyFlagRecord>> getFlagsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(flagService.getFlagsByEmployee(employeeId));
    }
}