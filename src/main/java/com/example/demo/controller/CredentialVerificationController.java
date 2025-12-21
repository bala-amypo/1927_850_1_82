package com.example.demo.controller;

import com.example.demo.dto.CredentialRequestDto;
import com.example.demo.dto.CredentialStatusDto;
import com.example.demo.model.Credential;
import com.example.demo.service.CredentialVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credentials")
public class CredentialVerificationController {

    private final CredentialVerificationService credentialService;

    public CredentialVerificationController(CredentialVerificationService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    public ResponseEntity<Credential> registerCredential(@RequestBody CredentialRequestDto dto) {
        Credential entity = new Credential();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setCredentialId(dto.getCredentialId());
        entity.setIssuer(dto.getIssuer());
        entity.setIssuedAt(dto.getIssuedAt());
        entity.setExpiresAt(dto.getExpiresAt());
        entity.setMetadataJson(dto.getMetadataJson());
        
        return new ResponseEntity<>(credentialService.registerCredential(entity), HttpStatus.CREATED);
    }

    @PostMapping("/{credentialId}/verify")
    public ResponseEntity<CredentialStatusDto> verifyCredential(@PathVariable String credentialId) {
        return ResponseEntity.ok(credentialService.verifyCredential(credentialId));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Credential>> getCredentialsForEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(credentialService.getCredentialsForEmployee(employeeId));
    }
}