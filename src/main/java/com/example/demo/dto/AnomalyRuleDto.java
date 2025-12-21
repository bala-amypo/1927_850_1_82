package com.example.demo.dto;

public class AnomalyRuleDto {
    private String ruleCode;
    private String description;
    private String thresholdType;
    private Double thresholdValue;
    private Boolean active;
    
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getThresholdType() { return thresholdType; }
    public void setThresholdType(String thresholdType) { this.thresholdType = thresholdType; }
    public Double getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(Double thresholdValue) { this.thresholdValue = thresholdValue; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}