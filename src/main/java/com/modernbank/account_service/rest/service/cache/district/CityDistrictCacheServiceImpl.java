package com.modernbank.account_service.rest.service.cache.district;

import com.modernbank.account_service.entity.City;
import com.modernbank.account_service.entity.District;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.CityModel;
import com.modernbank.account_service.model.DistrictModel;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.repository.DistrictRepository;
import com.modernbank.account_service.rest.service.IMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CityDistrictCacheServiceImpl implements CityDistrictCacheService {

    private final DistrictRepository districtRepository;

    private final IMapperService mapperService;

    private final BranchRepository branchRepository;

    @Cacheable(value = "districts", key = "#cityId")
    @Override
    public List<DistrictModel> getDistrictsByCityId(Long cityId) {
        List<District> districts = districtRepository.findDistrictByCityId(cityId)
                .orElseThrow(() -> new NotFoundException("District not found with cityId: " + cityId));

        return mapperService.map(districts, DistrictModel.class);
    }

    @Cacheable(value = "city")
    public List<CityModel> getCities() {
        List<City> cities = branchRepository.findCitiesWithActiveBranches()
                .orElseThrow(() -> new NotFoundException("Cities with active branches not found"));

        return mapperService.map(cities, CityModel.class); // Placeholder return statement
    }
}