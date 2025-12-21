package com.example.demo.repository;

import com.example.demo.model.CredentialVerificationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CredentialVerificationEventRepository extends JpaRepository<CredentialVerificationEvent, Long> {
    List<CredentialVerificationEvent> findByCredentialId(Long credentialId);
}