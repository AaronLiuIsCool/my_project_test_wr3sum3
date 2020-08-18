package com.kuaidaoresume.resume.controller.v1.api;

import com.github.pemistahl.lingua.api.LanguageDetector;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.resume.model.Keyword;
import com.kuaidaoresume.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class KeywordController {

    @Autowired
    private final ResumeService resumeService;

    @Autowired
    private final LanguageDetector languageDetector;

    @Authorize(value = {
        AuthConstant.AUTHORIZATION_WWW_SERVICE,
        AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
        //AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
        //AuthConstant.AUTHORIZATION_BOT_SERVICE,
        AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
        AuthConstant.AUTHORIZATION_SUPPORT_USER,
        AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/keywords")
    public ResponseEntity<?> saveAll(@RequestBody Collection<String> keywords) {
        resumeService.saveKeywords(keywords.stream().map(keyword ->
            Keyword.builder().value(keyword).language(languageDetector.detectLanguageOf(keyword).getIsoCode639_1().toString()).build()).collect(Collectors.toSet()));
        return ResponseEntity.noContent().build();
    }
}
