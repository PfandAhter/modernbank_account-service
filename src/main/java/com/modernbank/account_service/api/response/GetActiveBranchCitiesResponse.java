package com.modernbank.account_service.api.response;

import com.modernbank.account_service.api.dto.CityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetActiveBranchCitiesResponse extends BaseResponse{

    private List<CityDTO> cities;
}