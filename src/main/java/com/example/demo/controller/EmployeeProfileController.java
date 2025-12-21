package com.example.demo.controller;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.service.EmployeeProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeProfileController {

    private final EmployeeProfileService employeeService;

    public EmployeeProfileController(EmployeeProfileService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeProfile> createEmployee(@RequestBody EmployeeProfileDto dto) {
        EmployeeProfile entity = new EmployeeProfile();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setTeamName(dto.getTeamName());
        entity.setRole(dto.getRole());
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);
        return new ResponseEntity<>(employeeService.createEmployee(entity), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProfile> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<EmployeeProfile>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
}