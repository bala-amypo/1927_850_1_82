package com.example.demo.service.impl;

import com.example.demo.model.EmployeeProfile;
import com.example.demo.model.ProductivityMetricRecord;
import com.example.demo.model.TeamSummaryRecord;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.ProductivityMetricRecordRepository;
import com.example.demo.repository.TeamSummaryRecordRepository;
import com.example.demo.service.TeamSummaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TeamSummaryServiceImpl implements TeamSummaryService {

    private final TeamSummaryRecordRepository summaryRepository;
    private final ProductivityMetricRecordRepository metricRepository;
    private final EmployeeProfileRepository employeeRepository;

    public TeamSummaryServiceImpl(TeamSummaryRecordRepository summaryRepository,
                                  ProductivityMetricRecordRepository metricRepository,
                                  EmployeeProfileRepository employeeRepository) {
        this.summaryRepository = summaryRepository;
        this.metricRepository = metricRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public TeamSummaryRecord generateSummary(String teamName, LocalDate summaryDate) {
        List<EmployeeProfile> teamMembers = employeeRepository.findAll().stream()
                .filter(e -> teamName.equals(e.getTeamName()))
                .toList();

        double totalHours = 0;
        int totalTasks = 0;
        double totalScore = 0;
        int count = 0;

        for (EmployeeProfile member : teamMembers) {
            var metricOpt = metricRepository.findByEmployeeIdAndDate(member.getId(), summaryDate);
            if (metricOpt.isPresent()) {
                ProductivityMetricRecord m = metricOpt.get();
                totalHours += m.getHoursLogged();
                totalTasks += m.getTasksCompleted();
                totalScore += m.getProductivityScore();
                count++;
            }
        }

        TeamSummaryRecord summary = new TeamSummaryRecord();
        summary.setTeamName(teamName);
        summary.setSummaryDate(summaryDate);
        
        if (count > 0) {
            summary.setAvgHoursLogged(totalHours / count);
            summary.setAvgTasksCompleted((double) totalTasks / count);
            summary.setAvgScore(totalScore / count);
        } else {
            summary.setAvgHoursLogged(0.0);
            summary.setAvgTasksCompleted(0.0);
            summary.setAvgScore(0.0);
        }
        
        summary.setAnomalyCount(0); 
        return summaryRepository.save(summary);
    }

    @Override
    public List<TeamSummaryRecord> getSummariesByTeam(String teamName) {
        return summaryRepository.findByTeamName(teamName);
    }
}