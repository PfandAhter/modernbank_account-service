package com.modernbank.account_service.rest.service.cache.error;

import com.modernbank.account_service.api.client.ParameterServiceClient;
import com.modernbank.account_service.api.response.GetAllErrorCodesResponse;
import com.modernbank.account_service.api.response.GetErrorCodeResponse;
import com.modernbank.account_service.entity.ErrorCodes;
import com.modernbank.account_service.rest.service.MapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class ErrorCacheServiceImpl implements ErrorCacheService {


    private final MapperService mapperService;

    private final RedisTemplate<String, ErrorCodes> redisTemplate;

    private static final String ERROR_CODES_CACHE_KEY = "ERROR_CODE_CACHE";

    private final ParameterServiceClient parameterServiceClient;

    @Override
    public ErrorCodes getErrorCodeByErrorId(String code){
        String cacheKey = ERROR_CODES_CACHE_KEY + code;
        ErrorCodes errorCode = redisTemplate.opsForValue().get(cacheKey);

        if(errorCode != null){
            return errorCode;
        }
        GetErrorCodeResponse response;
        try{
            response = parameterServiceClient.getErrorCode(code);
        }catch (Exception e){
            return handleErrorGetFailed(code);
        }
        redisTemplate.opsForValue().set(cacheKey, mapperService.map(response.getErrorCode(), ErrorCodes.class));

        return mapperService.map(response.getErrorCode(), ErrorCodes.class);
    }

    @Scheduled(fixedRate = 3600000) // every hour
    @Override
    public void refreshAllErrorCodesCache() {
        try {
                        GetAllErrorCodesResponse allErrorCodes = parameterServiceClient.getAllErrorCodes("account-service");
            log.info("Refreshing error codes cache");

            allErrorCodes.getErrorCodes().forEach(errorCodeDTO-> {
                String code = errorCodeDTO.getError();
                String cacheKey = ERROR_CODES_CACHE_KEY + code;
                ErrorCodes errorCode = mapperService.map(errorCodeDTO, ErrorCodes.class);
                redisTemplate.opsForValue().set(cacheKey, errorCode);
            });
        } catch (Exception e) {
            log.error("Error refreshing error codes cache, keeping existing cache.", e);
        }
    }

    private ErrorCodes handleErrorGetFailed(String code){
        return ErrorCodes.builder()
                .id(code)
                .error("Not Found")
                .description("The requested resource was not found")
                .build();
    }
}