package com.example.demo.dto;

import java.time.LocalDateTime;

public class CredentialStatusDto {
    private String credentialId;
    private String status;
    private LocalDateTime verifiedAt;
    private String details;

    public String getCredentialId() { return credentialId; }
    public void setCredentialId(String credentialId) { this.credentialId = credentialId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}