package com.example.authservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingService {
    public void logMessage() {
        log.info("Đây là log INFO");
        log.error("Đây là log ERRORRRR");
        log.trace("day la log trace");
        log.debug("debuggg");
        log.warn("warnnnn");
    }
}
