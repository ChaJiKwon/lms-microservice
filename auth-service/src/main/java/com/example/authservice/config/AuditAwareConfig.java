package com.example.authservice.config;
import com.example.authservice.service.AuditAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditAwareConfig {
    @Bean
    AuditorAware<String> auditorProvider(){
        return new AuditAwareImpl();
    }
}
