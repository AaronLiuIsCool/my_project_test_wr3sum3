package com.kuaidaoresume.common.matching;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import java.util.Collection;
import java.util.stream.Collectors;

public class InMemoryKeywordMatcher implements KeywordMatcher {

    private final Trie keywordsTrie;

    public InMemoryKeywordMatcher(Collection<String> keywords) {
        keywordsTrie = Trie.builder()
            .ignoreOverlaps()
            .ignoreCase()
            .onlyWholeWords()
            .addKeywords(keywords)
            .build();
    }

    @Override
    public Collection<String> getMatches(String text) {
        return getEmits(text).stream().map(Emit::getKeyword).collect(Collectors.toSet());
    }

    private Collection<Emit> getEmits(String text) {
        Collection<Emit> emits = keywordsTrie.parseText(text);
        return emits;
    }

    @Override
    public boolean contains(String text) {
        return keywordsTrie.containsMatch(text);
    }
}
