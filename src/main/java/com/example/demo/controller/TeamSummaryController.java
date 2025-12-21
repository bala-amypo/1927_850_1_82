package com.example.demo.controller;

import com.example.demo.model.TeamSummaryRecord;
import com.example.demo.service.TeamSummaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/team-summaries")
public class TeamSummaryController {

    private final TeamSummaryService summaryService;

    public TeamSummaryController(TeamSummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @PostMapping("/generate")
    public ResponseEntity<TeamSummaryRecord> generateSummary(
            @RequestParam String teamName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(summaryService.generateSummary(teamName, date), HttpStatus.CREATED);
    }

    @GetMapping("/team/{teamName}")
    public ResponseEntity<List<TeamSummaryRecord>> getSummariesByTeam(@PathVariable String teamName) {
        return ResponseEntity.ok(summaryService.getSummariesByTeam(teamName));
    }
}