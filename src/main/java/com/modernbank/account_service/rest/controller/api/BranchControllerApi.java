package com.modernbank.account_service.rest.controller.api;

import com.modernbank.account_service.rest.controller.api.request.CreateBranchRequest;
import com.modernbank.account_service.rest.controller.api.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface BranchControllerApi {

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    ResponseEntity<BaseResponse> createBranch(@RequestBody CreateBranchRequest createBranchRequest);
}
