package com.kuaidaoresume.matching.client;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.matching.MatchingConstant;
import com.kuaidaoresume.matching.dto.CreateMatchingRequest;
import com.kuaidaoresume.matching.dto.GenericMatchingResponse;
import com.kuaidaoresume.matching.dto.MatchingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@FeignClient(name = MatchingConstant.SERVICE_NAME, path = "/v1", url = "${kuaidaoresume.matching-service-endpoint}")
public interface MatchingClient {

    @PostMapping(path = "/matchings")
    GenericMatchingResponse createMatching(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestBody @Valid CreateMatchingRequest request);

    @GetMapping(path = "/matchings")
    GenericMatchingResponse getMatching(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestParam @NotBlank String matchingId);

    @PutMapping(path = "/matchings")
    GenericMatchingResponse updateMatching(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestBody @Valid MatchingDto matchingDto);

    @DeleteMapping(path = "/matchings")
    GenericMatchingResponse deleteMatching(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authz, @RequestParam @NotBlank String matchingId);

    //TODO: others endpoints from Aaron Liu
}
