package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.api.request.citydistrict.CreateCityRequest;
import com.modernbank.account_service.api.request.citydistrict.CreateDistrictRequest;
import com.modernbank.account_service.api.request.citydistrict.UpdateCityRequest;
import com.modernbank.account_service.api.request.citydistrict.UpdateDistrictRequest;
import com.modernbank.account_service.entity.Branch;
import com.modernbank.account_service.entity.City;
import com.modernbank.account_service.entity.District;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.CityModel;
import com.modernbank.account_service.model.DistrictModel;
import com.modernbank.account_service.model.enums.Status;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.repository.CityRepository;
import com.modernbank.account_service.repository.DistrictRepository;
import com.modernbank.account_service.rest.service.CityDistrictService;
import com.modernbank.account_service.rest.service.IMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor

public class CityDistrictServiceImpl implements CityDistrictService {

    private final DistrictRepository districtRepository;

    private final CityRepository cityRepository;

    private final BranchRepository branchRepository;

    private final IMapperService mapperService;

    @Override
    public void createCity(CreateCityRequest request) {
        cityRepository.save(City.builder()
                .name(request.getName())
                .districts(new ArrayList<>())
                .status(Status.INACTIVE)
                .build());
    }

    @Override
    public void createDistrict(CreateDistrictRequest request) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new NotFoundException("City not found"));

        districtRepository.save(District.builder()
                .city(city)
                .branches(new ArrayList<>())
                .status(Status.INACTIVE)
                .name(request.getName())
                .build());
    }

    @Override
    public void updateDistrict(UpdateDistrictRequest request) {
        District district = districtRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("District not found"));

        if (request.getName() != null) {
            district.setName(request.getName());
        }

        if(request.getStatus() != null){
            district.setStatus(Status.valueOf(request.getStatus().toUpperCase()));
        }

        if(request.getBranchIds() != null){
            request.getBranchIds().values().forEach(branchId ->{
                Branch branch = branchRepository.findById(branchId)
                        .orElseThrow(() -> new NotFoundException("Branch not found"));
                district.getBranches().add(branch);
                branch.setDistrict(district);
                branchRepository.save(branch);
            });
        }
        districtRepository.save(district);
    }

    @Override
    public void updateCity(UpdateCityRequest request){
        City city = cityRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("City not found"));

        if(request.getName() != null){
            city.setName(request.getName());
        }
        if(request.getStatus() != null){
            city.setStatus(Status.valueOf(request.getStatus().toUpperCase()));
        }
        cityRepository.save(city);
    }

    @Override
    public CityModel getCityById(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException("City not found"));

        return mapperService.map(city, CityModel.class);
    }

    @Override
    public DistrictModel getDistrictById(Long districtId) {
        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new NotFoundException("District not found"));

        return mapperService.map(district, DistrictModel.class);
    }
}