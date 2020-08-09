package com.kuaidaoresume.matching.controller;


import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.matching.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/matching")
@Validated
public class MatchingController<MatchingService> {

    static final ILogger logger = SLoggerFactory.getLogger(MatchingController.class);

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private EnvConfig envConfig;
}
