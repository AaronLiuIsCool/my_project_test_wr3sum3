package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.KeywordDto;
import com.kuaidaoresume.job.model.Keyword;
import com.kuaidaoresume.job.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService{

    @Autowired
    private final KeywordRepository keywordRepository;
    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public Optional<KeywordDto> findKeywordById(long id) {
        Optional<Keyword> keywordOptional = keywordRepository.findById(id);
        if (!keywordOptional.isPresent()) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(modelMapper.map(keywordOptional.get(), KeywordDto.class));
    }

    @Override
    public Optional<KeywordDto> findKeywordByName(String name) {
        Optional<Keyword> keywordOptional = keywordRepository.findByName(name);
        if (!keywordOptional.isPresent()) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(modelMapper.map(keywordOptional.get(), KeywordDto.class));
    }

    @Override
    public void deleteKeywordById(long id)  {
        keywordRepository.deleteById(id);
    }

    @Override
    public void deleteKeywordByName(String name)  {
        keywordRepository.deleteByName(name);
    }

    @Override
    public  Optional<KeywordDto> createKeyword(KeywordDto keywordDto) {

        Keyword keyword = modelMapper.map(keywordDto, Keyword.class);

        KeywordDto savedKeywordDto = modelMapper.map(keywordRepository.save(keyword), KeywordDto.class);

        return Optional.ofNullable(savedKeywordDto);

    }

    @Override
    public List<KeywordDto> findAll(){
        List<Keyword> keywords = keywordRepository.findAll();
        List<KeywordDto> keywordDtos = keywords.stream()
                .map(x -> modelMapper.map(x, KeywordDto.class))
                .collect(Collectors.toList());
        return keywordDtos;
    }

}
