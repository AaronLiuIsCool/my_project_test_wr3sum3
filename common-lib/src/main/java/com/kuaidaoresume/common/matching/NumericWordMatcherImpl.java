package com.kuaidaoresume.common.matching;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.apache.commons.compress.utils.Sets;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumericWordMatcherImpl implements NumericWordMatcher {
    private final Trie numericWordsTrie;
    private final Pattern digitsPattern;
    private final Set<Character> punctuations = Sets.newHashSet('.', ',', ';', ':', '?', '!');
    private final boolean matchCardinal;

    public NumericWordMatcherImpl() {
        this(false);
    }
    public NumericWordMatcherImpl(boolean matchCardinal) {
        this.matchCardinal = matchCardinal;
        digitsPattern = Pattern.compile("[-\\+$]?(\\d+|\\d{1,3}(,\\d{3})*)(\\.\\d+)?%?([\\.,;!\\+])?");
        List<String> numericWords = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("matching/numeric-words.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String numericWord;
            while ((numericWord = br.readLine()) != null) {
                numericWords.add(numericWord);
            }
            numericWordsTrie = Trie.builder()
            .ignoreCase()
            .onlyWholeWords()
            .addKeywords(numericWords)
            .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean contains(String text) {
        return numericWordsTrie.containsMatch(text) ||
            tokenizeBySpace(text).parallel().anyMatch(token -> isDigits(token));
    }

    private Stream<String> tokenizeBySpace(String text) {
        return Pattern.compile("\\s").splitAsStream(text);
    }

    private boolean isDigits(String word) {
        return digitsPattern.matcher(word).matches();
    }

    @Override
    public Collection<String> getMatches(String text) {
        Set<String> numericWords = new HashSet<>();
        numericWords.addAll(getDigitMatches(text));
        if (matchCardinal) {
            numericWords.addAll(getCardinalMatches(text));
        }
        return numericWords;
    }

    private Collection<String> getCardinalMatches(String text) {
        return numericWordsTrie.parseText(text).stream()
            .map(Emit::getKeyword)
            .collect(Collectors.toSet());
    }

    private Collection<String> getDigitMatches(String text) {
        return tokenizeBySpace(text)
            .filter(word -> digitsPattern.matcher(word).matches())
            .map(word -> punctuations.contains(word.charAt(word.length() - 1)) ? word.substring(0, word.length() - 1) : word)
            .collect(Collectors.toSet());
    }
}
