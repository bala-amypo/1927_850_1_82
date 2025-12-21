package com.example.demo.service;

import com.example.demo.model.EmployeeProfile;
import java.util.List;
import java.util.Optional;

public interface EmployeeProfileService {
    EmployeeProfile createEmployee(EmployeeProfile employee);
    EmployeeProfile getEmployeeById(Long id);
    List<EmployeeProfile> getAllEmployees();
    Optional<EmployeeProfile> findByEmployeeId(String employeeId);
    EmployeeProfile updateEmployeeStatus(Long id, boolean active);
}