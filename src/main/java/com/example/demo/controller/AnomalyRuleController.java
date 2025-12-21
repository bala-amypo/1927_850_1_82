package com.example.demo.controller;

import com.example.demo.dto.AnomalyRuleDto;
import com.example.demo.model.AnomalyRule;
import com.example.demo.service.AnomalyRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anomaly-rules")
public class AnomalyRuleController {

    private final AnomalyRuleService ruleService;

    public AnomalyRuleController(AnomalyRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<AnomalyRule> createRule(@RequestBody AnomalyRuleDto dto) {
        AnomalyRule entity = new AnomalyRule();
        entity.setRuleCode(dto.getRuleCode());
        entity.setDescription(dto.getDescription());
        entity.setThresholdType(dto.getThresholdType());
        entity.setThresholdValue(dto.getThresholdValue());
        entity.setActive(dto.getActive());
        
        return new ResponseEntity<>(ruleService.createRule(entity), HttpStatus.CREATED);
    }

    @GetMapping("/active")
    public ResponseEntity<List<AnomalyRule>> getActiveRules() {
        return ResponseEntity.ok(ruleService.getActiveRules());
    }
}