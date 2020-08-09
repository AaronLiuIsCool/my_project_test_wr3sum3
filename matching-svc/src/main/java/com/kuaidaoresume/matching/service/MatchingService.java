package com.kuaidaoresume.matching.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.matching.dto.MatchingDto;
import com.kuaidaoresume.matching.repo.MatchingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
public class MatchingService {

    static ILogger logger = SLoggerFactory.getLogger(MatchingService.class);

    private final MatchingRepo matchingRepo;

    private final EnvConfig envConfig;

    @PersistenceContext
    private EntityManager entityManager;

    public MatchingDto create() {
        return null;
    }
}
