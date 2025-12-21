package com.example.demo.repository;

import com.example.demo.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    List<Credential> findByEmployeeId(Long employeeId);
    Optional<Credential> findByCredentialId(String credentialId);
}