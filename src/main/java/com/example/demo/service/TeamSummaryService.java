package com.example.demo.service;

import com.example.demo.model.TeamSummaryRecord;
import java.time.LocalDate;
import java.util.List;

public interface TeamSummaryService {
    TeamSummaryRecord generateSummary(String teamName, LocalDate summaryDate);
    List<TeamSummaryRecord> getSummariesByTeam(String teamName);
}