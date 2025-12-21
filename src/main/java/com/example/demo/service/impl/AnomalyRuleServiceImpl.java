package com.example.demo.service.impl;

import com.example.demo.model.AnomalyRule;
import com.example.demo.repository.AnomalyRuleRepository;
import com.example.demo.service.AnomalyRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AnomalyRuleServiceImpl implements AnomalyRuleService {

    private final AnomalyRuleRepository ruleRepository;

    public AnomalyRuleServiceImpl(AnomalyRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public AnomalyRule createRule(AnomalyRule rule) {
        if (ruleRepository.findByRuleCode(rule.getRuleCode()).isPresent()) {
            throw new IllegalStateException("Rule with this code already exists");
        }
        return ruleRepository.save(rule);
    }

    @Override
    public List<AnomalyRule> getActiveRules() {
        return ruleRepository.findByActiveTrue();
    }
}