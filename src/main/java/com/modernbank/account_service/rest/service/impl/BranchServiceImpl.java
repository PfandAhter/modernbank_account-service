package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.api.request.UpdateBranchRequest;
import com.modernbank.account_service.entity.Branch;
import com.modernbank.account_service.entity.District;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.BranchModel;
import com.modernbank.account_service.model.CityModel;
import com.modernbank.account_service.model.DistrictModel;
import com.modernbank.account_service.model.enums.Status;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.api.request.CreateBranchRequest;
import com.modernbank.account_service.rest.service.CityDistrictService;
import com.modernbank.account_service.rest.service.BranchService;
import com.modernbank.account_service.rest.service.cache.branch.BranchCacheService;
import com.modernbank.account_service.rest.service.cache.district.CityDistrictCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.modernbank.account_service.constants.ErrorCodeConstants.BRANCH_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    private final BranchCacheService branchCacheService;

    private final CityDistrictCacheService cityDistrictCacheService;

    private final CityDistrictService cityDistrictService;

    @Override
    public void createBranch(CreateBranchRequest request){
        District district = cityDistrictService.getDistrictById(request.getDistrictId());

        branchRepository.save(Branch.builder()
                .name(request.getName())
                .address(request.getAddress())
                .status(Status.INACTIVE)
                .district(district)
                .accounts(new ArrayList<>())
                .build());
    }

    @Override
    public void updateBranch(UpdateBranchRequest request) {
        Branch branch = branchRepository.findBranchById(request.getId())
                .orElseThrow(() -> new NotFoundException(BRANCH_NOT_FOUND));

        if(request.getName() != null){
            branch.setName(request.getName());
        }
        if(request.getAddress() != null){
            branch.setAddress(request.getAddress());
        }
        if(request.getStatus() != null){
            branch.setStatus(Status.valueOf(request.getStatus()));
        }
        if(request.getDistrictId() != null){
            District district = cityDistrictService.getDistrictById(request.getDistrictId());
            branch.setDistrict(district);
        }
        branchRepository.save(branch);
    }

    @Override
    public List<CityModel> getAllCities(){
        cityDistrictCacheService.evictCityCacheValues();
        return cityDistrictCacheService.getCities();
    }

    @Override
    public List<DistrictModel> getDistrictsByCityId(Long cityId){
        cityDistrictCacheService.evictDistrictCacheValues();
        return cityDistrictCacheService.getDistrictsByCityId(cityId);
    }

    @Override
    public List<BranchModel> getAllBranchesByDistrictId(Long districtId){
        branchCacheService.evictBranchCacheValues();
        return branchCacheService.getBranchCacheValuesByDistrictId(districtId);
    }
}