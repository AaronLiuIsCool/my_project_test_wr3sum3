package com.kuaidaoresume.job.controller.v1;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.job.controller.assembler.KeywordRepresentationModelAssembler;
import com.kuaidaoresume.job.dto.PersistedKeywordDto;
import com.kuaidaoresume.job.dto.KeywordDto;
import com.kuaidaoresume.job.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class KeywordController {

    static final ILogger logger = SLoggerFactory.getLogger(KeywordController.class);

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private final KeywordRepresentationModelAssembler keywordRepresentationModelAssembler;

    @Autowired
    private final ModelMapper modelMapper;

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/keywords/{id}")
    public ResponseEntity<EntityModel<PersistedKeywordDto>> findJob(@PathVariable long id) {
        return keywordService.findKeywordById(id)
                .map(keyword -> modelMapper.map(keyword, PersistedKeywordDto.class))
                .map(keywordRepresentationModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/keywords/id/{id}")
    public ResponseEntity<?> deleteKeyword(@PathVariable Long id) {
        keywordService.deleteKeywordById(id);
        return ResponseEntity.noContent().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/keywords/name/{name}")
    public ResponseEntity<?> deleteKeyword(@PathVariable String name) {
        keywordService.deleteKeywordByName(name);
        return ResponseEntity.noContent().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping(path = "/keywords/all")
    public ResponseEntity<CollectionModel<EntityModel<PersistedKeywordDto>>> findAllJobs() {
        List<KeywordDto> allKeywords = keywordService.findAll();
        if(allKeywords != null && allKeywords.size() > 0) {
            CollectionModel<EntityModel<PersistedKeywordDto>> collectionModel = keywordRepresentationModelAssembler.
                    toCollectionModel((allKeywords.stream().map(keywordDto -> modelMapper.map(keywordDto, PersistedKeywordDto.class))
                            .collect(Collectors.toList())));
            return ResponseEntity.ok(CollectionModel.of(collectionModel.getContent()));
        }
        return ResponseEntity.notFound().build();
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/keywords")
    public ResponseEntity<EntityModel<PersistedKeywordDto>> createKeyword(
            @Valid @RequestBody
                    KeywordDto keywordDto) {
        logger.info("keywordDto = " + keywordDto);
        Optional<KeywordDto> newKeyword = keywordService.createKeyword(keywordDto);
        if(newKeyword.isPresent()) {
            EntityModel<PersistedKeywordDto> entityModel =
                    keywordRepresentationModelAssembler.toModel(modelMapper.map(newKeyword, PersistedKeywordDto.class));
            return ResponseEntity.created(keywordRepresentationModelAssembler.getSelfLink(entityModel).toUri()).body(entityModel);
        }
        return ResponseEntity.badRequest().build();
    }
}
