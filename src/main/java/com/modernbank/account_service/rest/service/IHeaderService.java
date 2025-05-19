package com.modernbank.account_service.rest.service;

public interface IHeaderService {
    String extractToken();

    String extractUserId();

    String extractUserRole();
}

