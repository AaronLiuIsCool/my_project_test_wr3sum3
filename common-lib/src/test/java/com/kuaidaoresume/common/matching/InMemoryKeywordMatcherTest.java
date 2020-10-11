package com.kuaidaoresume.common.matching;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileInputStream;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InMemoryKeywordMatcherTest {

    private InMemoryKeywordMatcher inMemoryKeywordMatcher;
    private List<String> keywords;
    private String[] punctuations;
    private Random random;

    @BeforeAll
    public void init() throws Exception {
        random = new Random();
        punctuations = new String[]{", ", ". ", "; ", "! "};
        FileInputStream fis = new FileInputStream(Thread.currentThread().getContextClassLoader()
            .getResource("matching/keywords.xlsx").getFile());
        XSSFWorkbook keywordsWorkBook = new XSSFWorkbook(fis);
        XSSFSheet keywordsSheet = keywordsWorkBook.getSheetAt(0);
        Iterator<Row> rowIterator = keywordsSheet.iterator();
        Set<String> keywordsSet = new HashSet<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                keywordsSet.add(cell.getStringCellValue());
            }
        }
        keywords = new ArrayList<>(keywordsSet);
        inMemoryKeywordMatcher = new InMemoryKeywordMatcher(keywords);
    }

    @Test
    public void whenContainsWithNoMatching_thenReturnFalse() {
        assertFalse(inMemoryKeywordMatcher.contains("blah blah blah"));
    }

    @Test
    public void whenContainsWithMatching_thenReturnTrue() {
        StringBuilder textBuilder = new StringBuilder();
        int numHits = 3;
        int numKeywords = keywords.size();
        for (int i = 0; i < numHits; i++) {
            appendNonMatchingWords(textBuilder);

            String randomKeyword = keywords.get(random.nextInt(numKeywords));
            textBuilder.append(randomKeyword);

            appendRandomPunctuation(textBuilder);
        }
        String text = textBuilder.toString();
        assertTrue(inMemoryKeywordMatcher.contains(text));
    }

    @Test
    public void whenGetMatchesWithNoMatching_thenReturnEmptyList() {
        assertThat(inMemoryKeywordMatcher.getMatches("blah blah blah").size(), is(0));
    }

    @Test
    public void whenContainsWithChineseKeyword_thenReturnTrue() {
        assertTrue(inMemoryKeywordMatcher.contains("我曾经担任市场调研的工作。"));
    }

    @Test
    public void getMatchesWithChineseKeyword() {
        assertThat(inMemoryKeywordMatcher.getMatches("我曾经担任市场调研和软件工程的工作。"), hasItems("软件工程", "市场调研"));
    }

    @Test
    public void whenGetMatchesWithMatching_thenReturnMatchedWords() {
        StringBuilder textBuilder = new StringBuilder();
        int numHits = 3;
        int numKeywords = keywords.size();
        List<String> keywordsInText = new ArrayList<>();
        for (int i = 0; i < numHits; i++) {
            appendNonMatchingWords(textBuilder);

            String randomKeyword = keywords.get(random.nextInt(numKeywords));
            textBuilder.append(i % 2 == 0 ? randomKeyword.toLowerCase() : randomKeyword.toUpperCase());
            keywordsInText.add(randomKeyword);

            appendRandomPunctuation(textBuilder);
        }
        String text = textBuilder.toString();
        Collection<String> matches = inMemoryKeywordMatcher.getMatches(text);
        assertThat(matches.size(), is(numHits));
        for (String keyword : keywordsInText) {
            matches.contains(keyword.toLowerCase());
        }
    }

    private void appendNonMatchingWords(StringBuilder builder) {
        for (int i = 0; i < random.nextInt(5); i++) {
            builder.append("blah");
            if (builder.length() % 3 == 0) {
                appendRandomPunctuation(builder);
            } else {
                builder.append(" ");
            }
        }
    }

    private void appendRandomPunctuation(StringBuilder builder) {
        builder.append(punctuations[random.nextInt(punctuations.length)]);
    }
}
