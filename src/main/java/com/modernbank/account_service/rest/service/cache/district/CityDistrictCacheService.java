package com.modernbank.account_service.rest.service.cache.district;

import com.modernbank.account_service.model.CityModel;
import com.modernbank.account_service.model.DistrictModel;

import java.util.List;

public interface CityDistrictCacheService {
    List<DistrictModel> getDistrictsByCityId(Long cityId);

    List<CityModel> getCities();

    void evictCityCacheValues();

    void evictDistrictCacheValues();
}