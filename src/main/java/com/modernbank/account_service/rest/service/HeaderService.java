package com.modernbank.account_service.rest.service;

public interface HeaderService {
    String extractToken();

    String extractUserEmail();

    String extractUserId();

    String extractUserRole();

    String extractCorrelationId();
}

