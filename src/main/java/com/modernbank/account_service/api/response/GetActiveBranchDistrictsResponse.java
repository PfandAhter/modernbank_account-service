package com.modernbank.account_service.api.response;

import com.modernbank.account_service.api.dto.DistrictDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class GetActiveBranchDistrictsResponse extends BaseResponse{
    private List<DistrictDTO> districts;
}