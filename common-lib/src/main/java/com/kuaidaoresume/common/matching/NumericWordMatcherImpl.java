package com.kuaidaoresume.common.matching;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.apache.commons.compress.utils.Sets;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.pemistahl.lingua.api.IsoCode639_1.EN;
import static com.github.pemistahl.lingua.api.IsoCode639_1.ZH;

public class NumericWordMatcherImpl implements NumericWordMatcher {
    private final Trie numericWordsTrie;
    private final Pattern digitsPattern;
    private final Set<Character> punctuations = Sets.newHashSet('.', ',', ';', ':', '?', '!');
    private final boolean matchCardinal;
    private final LanguageDetector languageDetector;

    public NumericWordMatcherImpl() {
        this(false);
    }
    public NumericWordMatcherImpl(boolean matchCardinal) {
        this.languageDetector = LanguageDetectorBuilder.fromIsoCodes639_1(EN, ZH).build();
        this.matchCardinal = matchCardinal;
        digitsPattern = Pattern.compile("[-\\+$¥]?(\\d+|\\d{1,3}(,\\d{3})*)(\\.\\d+)?%?([\\.,;!\\+])?");
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
        return numericWordsTrie.containsMatch(text) || digitsPattern.matcher(text).find();
    }

    private Stream<String> tokenizeBySpace(String text) {
        return Pattern.compile("\\s").splitAsStream(text);
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
        if (languageDetector.detectLanguageOf(text) == Language.CHINESE) {
            Set<String> digits = new HashSet<>();
            Matcher matcher = digitsPattern.matcher(text);
            while (matcher.find()) {
                digits.add(matcher.group());
            }
            return digits;
        } else {
            return tokenizeBySpace(text)
                .filter(word -> digitsPattern.matcher(word).matches())
                .map(word -> punctuations.contains(word.charAt(word.length() - 1)) ? word.substring(0, word.length() - 1) : word)
                .collect(Collectors.toSet());
        }
    }
}
