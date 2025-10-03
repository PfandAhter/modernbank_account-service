package com.modernbank.account_service.rest.service.branch;

import com.modernbank.account_service.model.entity.Branch;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.rest.controller.api.request.CreateBranchRequest;
import com.modernbank.account_service.rest.controller.api.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchServiceImpl implements IBranchService{

    private final BranchRepository branchRepository;

    public BaseResponse createBranch(CreateBranchRequest request){
        branchRepository.save(Branch.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .active(1)
                .accounts(new ArrayList<>())
                .build());
        return new BaseResponse("Branch created successfully");
    }
}
