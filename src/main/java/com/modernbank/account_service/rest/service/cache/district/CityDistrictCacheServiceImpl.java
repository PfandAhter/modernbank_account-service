package com.modernbank.account_service.rest.service.cache.district;

import com.modernbank.account_service.entity.City;
import com.modernbank.account_service.entity.District;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.CityModel;
import com.modernbank.account_service.model.DistrictModel;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.repository.CityRepository;
import com.modernbank.account_service.repository.DistrictRepository;
import com.modernbank.account_service.rest.service.MapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityDistrictCacheServiceImpl implements CityDistrictCacheService {

    private final DistrictRepository districtRepository;

    private final MapperService mapperService;

    private final CityRepository cityRepository;

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
        List<City> cities = cityRepository.findAllActiveCities()
                .orElseThrow(() -> new NotFoundException("Cities with active branches not found"));

        return mapperService.map(cities, CityModel.class); // Placeholder return statement
    }

    @Override
    @CacheEvict(value = "city", allEntries = true)
    public void evictCityCacheValues() {
        log.info("Cache for cities has been evicted.");
    }

    @Override
    @CacheEvict(value = "districts", allEntries = true)
    public void evictDistrictCacheValues() {
        log.info("Cache for districts has been evicted.");
    }
}