package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.entity.Branch;
import com.modernbank.account_service.entity.District;
import com.modernbank.account_service.model.CityModel;
import com.modernbank.account_service.model.DistrictModel;
import com.modernbank.account_service.model.enums.Status;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.api.request.CreateBranchRequest;
import com.modernbank.account_service.rest.service.CityDistrictService;
import com.modernbank.account_service.rest.service.IMapperService;
import com.modernbank.account_service.rest.service.IBranchService;
import com.modernbank.account_service.rest.service.cache.branch.BranchCacheService;
import com.modernbank.account_service.rest.service.cache.district.CityDistrictCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchServiceImpl implements IBranchService {

    private final BranchRepository branchRepository;

    private final IMapperService mapperService;

    private final BranchCacheService branchCacheService;

    private final CityDistrictCacheService cityDistrictCacheService;

    private final CityDistrictService cityDistrictService;

    public void createBranch(CreateBranchRequest request){

        District district = mapperService.map(cityDistrictService.getDistrictById(request.getDistrictId()), District.class);

        branchRepository.save(Branch.builder()
                .name(request.getName())
                .address(request.getAddress())
                .status(Status.INACTIVE)
                .district(district)
                .accounts(new ArrayList<>())
                .build());
    }

    public List<CityModel> getAllCities(){
        return null; // TODO: enable when cache is ready
//        return branchCacheService.getCities();
    }

    public List<DistrictModel> getDistrictsByCityId(Long cityId){
        return cityDistrictCacheService.getDistrictsByCityId(cityId);
    }
}