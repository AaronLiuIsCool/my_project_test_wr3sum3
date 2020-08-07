package com.kuaidaoresume.common.matching;

import org.ahocorasick.trie.Trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class NumericWordMatcherImpl implements NumericWordMatcher {
    private final Trie numericWordsTrie;
    private final Pattern digitsPattern;

    public NumericWordMatcherImpl() {
        digitsPattern = Pattern.compile("[-\\+$]?\\d+(\\.\\d+)?%?([\\.,;!\\+])?");
        List<String> numericWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(Thread.currentThread().getContextClassLoader()
            .getResource("matching/numeric-words.txt").getFile())))) {
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
}
