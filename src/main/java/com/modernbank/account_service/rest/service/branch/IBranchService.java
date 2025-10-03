package com.modernbank.account_service.rest.service.branch;

import com.modernbank.account_service.rest.controller.api.request.CreateBranchRequest;
import com.modernbank.account_service.rest.controller.api.response.BaseResponse;

public interface IBranchService {

    BaseResponse createBranch(CreateBranchRequest request);
}
