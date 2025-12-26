package com.example.demo.util;

import org.springframework.stereotype.Component;

@Component
public class ProductivityCalculator {
    
    private static final double HOURS_WEIGHT = 10.0;
    private static final double TASKS_WEIGHT = 5.0;
    private static final double MEETINGS_WEIGHT = 2.0;
    private static final double MAX_SCORE = 100.0;
    private static final double MIN_SCORE = 0.0;
    
    public double calculateProductivityScore(Double hoursLogged, Integer tasksCompleted, Integer meetingsAttended) {
        if (hoursLogged == null || hoursLogged < 0) hoursLogged = 0.0;
        if (tasksCompleted == null || tasksCompleted < 0) tasksCompleted = 0;
        if (meetingsAttended == null || meetingsAttended < 0) meetingsAttended = 0;
        
        double rawScore = (hoursLogged * HOURS_WEIGHT) + 
                         (tasksCompleted * TASKS_WEIGHT) + 
                         (meetingsAttended * MEETINGS_WEIGHT);
        
        return Math.max(MIN_SCORE, Math.min(MAX_SCORE, rawScore));
    }
    
    public boolean isAnomalousScore(double score, double averageScore) {
        double threshold = averageScore * 0.3; // 30% deviation threshold
        return Math.abs(score - averageScore) > threshold;
    }
    
    public static double computeScore(double hoursLogged, int tasksCompleted, int meetingsAttended) {
        // Handle invalid inputs by returning 0
        if (Double.isNaN(hoursLogged) || hoursLogged < 0) {
            return 0.0;
        }
        if (tasksCompleted < 0 || meetingsAttended < 0) {
            return 0.0;
        }
        
        // Special case: too many meetings should reduce score
        if (meetingsAttended > 10) {
            return Math.max(MIN_SCORE, 50.0 - (meetingsAttended - 10) * 2.0);
        }
        
        double rawScore = (hoursLogged * HOURS_WEIGHT) + 
                         (tasksCompleted * TASKS_WEIGHT) + 
                         (meetingsAttended * MEETINGS_WEIGHT);
        
        return Math.max(MIN_SCORE, Math.min(MAX_SCORE, rawScore));
    }
}