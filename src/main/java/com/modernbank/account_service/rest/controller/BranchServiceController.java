package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.rest.controller.api.BranchControllerApi;
import com.modernbank.account_service.rest.controller.api.request.CreateBranchRequest;
import com.modernbank.account_service.rest.controller.api.response.BaseResponse;
import com.modernbank.account_service.rest.service.branch.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/branch")
@RequiredArgsConstructor
@CrossOrigin
public class BranchServiceController implements BranchControllerApi {

    private final IBranchService branchService;

    @Override
    public ResponseEntity<BaseResponse> createBranch(CreateBranchRequest createBranchRequest) {
        return ResponseEntity.ok(branchService.createBranch(createBranchRequest));
    }
}