package com.modernbank.account_service.rest.service.cache.branch;

import com.modernbank.account_service.model.BranchModel;

import java.util.List;

public interface BranchCacheService {

    List<BranchModel> getBranchCacheValuesByDistrictId(Long districtId);

    void evictBranchCacheValues();
}