package com.modernbank.account_service.rest.service.cache.branch;


import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.BranchModel;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.rest.service.MapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.modernbank.account_service.constants.ErrorCodeConstants.BRANCH_NOT_FOUND_BY_DISTRICT_ID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BranchCacheServiceImpl implements BranchCacheService {

    private final BranchRepository branchRepository;

    private final MapperService mapperService;

    @Cacheable(value = "branch", key = "#districtId")
    @Override
    public List<BranchModel> getBranchCacheValuesByDistrictId(Long districtId) {
        return mapperService.map(branchRepository.findBranchesByDistrictId(districtId)
                .orElseThrow(() -> new NotFoundException(BRANCH_NOT_FOUND_BY_DISTRICT_ID)), BranchModel.class); // TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.
    }

    @CacheEvict(value = "branch", allEntries = true)
    @Override
    public void evictBranchCacheValues() {
        log.info("Branch cache cleared");
    }
}