package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.KeywordDto;

import java.util.List;
import java.util.Optional;

public interface KeywordService {
    public Optional<KeywordDto> findKeywordById(long id);

    public Optional<KeywordDto> findKeywordByName(String name);

    public void deleteKeywordById(long id);

    public void deleteKeywordByName(String name);

    public List<KeywordDto> findAll();

    public Optional<KeywordDto> createKeyword(KeywordDto keywordDto);

}
