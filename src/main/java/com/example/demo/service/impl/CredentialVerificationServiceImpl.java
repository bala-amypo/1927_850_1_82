package com.example.demo.service.impl;

import com.example.demo.dto.CredentialStatusDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Credential;
import com.example.demo.model.CredentialVerificationEvent;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.repository.CredentialRepository;
import com.example.demo.repository.CredentialVerificationEventRepository;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.service.CredentialVerificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CredentialVerificationServiceImpl implements CredentialVerificationService {

    private final CredentialRepository credentialRepository;
    private final CredentialVerificationEventRepository eventRepository;
    private final EmployeeProfileRepository employeeRepository;

    public CredentialVerificationServiceImpl(CredentialRepository credentialRepository,
                                             CredentialVerificationEventRepository eventRepository,
                                             EmployeeProfileRepository employeeRepository) {
        this.credentialRepository = credentialRepository;
        this.eventRepository = eventRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Credential registerCredential(Credential credential) {
        EmployeeProfile emp = employeeRepository.findById(credential.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (!Boolean.TRUE.equals(emp.getActive())) {
            throw new IllegalStateException("Employee not active");
        }

        if (credentialRepository.findByCredentialId(credential.getCredentialId()).isPresent()) {
            throw new IllegalStateException("Credential mapping exists");
        }

        credential.setStatus("PENDING");
        return credentialRepository.save(credential);
    }

    @Override
    public CredentialStatusDto verifyCredential(String credentialId) {
        Credential cred = credentialRepository.findByCredentialId(credentialId)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));

        boolean valid = true;
        String resultDetails = "Verification Successful";
        
        if (cred.getExpiresAt() != null && cred.getExpiresAt().isBefore(LocalDateTime.now())) {
            valid = false;
            resultDetails = "Credential expired";
            cred.setStatus("EXPIRED");
        } else {
            cred.setStatus("VERIFIED");
        }
        
        credentialRepository.save(cred);

        CredentialVerificationEvent event = new CredentialVerificationEvent();
        event.setCredentialId(cred.getId());
        event.setResult(valid ? "SUCCESS" : "FAILURE");
        event.setDetails(resultDetails);
        eventRepository.save(event);

        CredentialStatusDto dto = new CredentialStatusDto();
        dto.setCredentialId(credentialId);
        dto.setStatus(cred.getStatus());
        dto.setVerifiedAt(LocalDateTime.now());
        dto.setDetails(resultDetails);
        
        return dto;
    }

    @Override
    public List<Credential> getCredentialsForEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found");
        }
        return credentialRepository.findByEmployeeId(employeeId);
    }
}