package com.modernbank.account_service.rest.service.cache.error;

import com.modernbank.account_service.exception.ErrorCodesNotFoundException;
import com.modernbank.account_service.entity.ErrorCodes;
import com.modernbank.account_service.repository.ErrorCodesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class ErrorCacheServiceImpl implements IErrorCacheService{

    private final ErrorCodesRepository errorCodesRepository;

    @Cacheable(value = "errorCodes", key = "'all'")
    @Override
    public List<ErrorCodes> getAllErrorCodes(){
        return errorCodesRepository.findAll();
    }

    @Cacheable(value = "errorCode", key = "#errorCodeId")
    @Override
    public ErrorCodes getErrorCode(String errorCodeId){
        return errorCodesRepository.findErrorCodesById(errorCodeId)
                .orElseThrow(() -> new ErrorCodesNotFoundException(errorCodeId, LocalDateTime.now()));
    }

    @CacheEvict(value = {"errorCodes","errorCode"}, allEntries = true)
    public void refreshErrorCodesCache(){
        log.info("Error Codes Cache has been refreshed");
    }
}