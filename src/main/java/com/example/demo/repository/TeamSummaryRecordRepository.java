package com.example.demo.repository;

import com.example.demo.model.TeamSummaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeamSummaryRecordRepository extends JpaRepository<TeamSummaryRecord, Long> {
    List<TeamSummaryRecord> findByTeamName(String teamName);
}