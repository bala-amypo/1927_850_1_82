package com.example.demo.service;

import com.example.demo.dto.CredentialStatusDto;
import com.example.demo.model.Credential;
import java.util.List;

public interface CredentialVerificationService {
    Credential registerCredential(Credential credential);
    CredentialStatusDto verifyCredential(String credentialId);
    List<Credential> getCredentialsForEmployee(Long employeeId);
}