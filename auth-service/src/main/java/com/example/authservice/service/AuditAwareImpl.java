package com.example.authservice.service;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
public class AuditAwareImpl implements AuditorAware<String> {
    @Override
    public @NonNull Optional<String> getCurrentAuditor() {
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("User email: " + userEmail);
        return Optional.of(userEmail);
    }
}
