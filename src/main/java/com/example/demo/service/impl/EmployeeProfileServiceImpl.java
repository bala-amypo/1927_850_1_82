package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.service.EmployeeProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;

    public EmployeeProfileServiceImpl(EmployeeProfileRepository employeeProfileRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public EmployeeProfile createEmployee(EmployeeProfile employee) {
        if (employeeProfileRepository.findByEmployeeId(employee.getEmployeeId()).isPresent()) {
            throw new IllegalStateException("Employee with this employeeId already exists"); 
        }
        return employeeProfileRepository.save(employee);
    }

    @Override
    public EmployeeProfile getEmployeeById(Long id) {
        return employeeProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found")); 
    }

    @Override
    public List<EmployeeProfile> getAllEmployees() {
        return employeeProfileRepository.findAll();
    }

    @Override
    public Optional<EmployeeProfile> findByEmployeeId(String employeeId) {
        return employeeProfileRepository.findByEmployeeId(employeeId);
    }

    @Override
    public EmployeeProfile updateEmployeeStatus(Long id, boolean active) {
        EmployeeProfile employee = getEmployeeById(id); 
        employee.setActive(active);
        return employeeProfileRepository.save(employee);
    }
}