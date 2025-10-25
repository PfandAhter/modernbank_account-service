package com.modernbank.account_service.rest.service.cache.error;

import com.modernbank.account_service.entity.ErrorCodes;

import java.util.List;

public interface ErrorCacheService {
    ErrorCodes getErrorCodeByErrorId(String code);

    void refreshAllErrorCodesCache();
}