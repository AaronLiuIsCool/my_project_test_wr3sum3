package com.kuaidaoresume.common.matching;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumericWordMatcherImplTest {
    private NumericWordMatcherImpl numericWordMatcherImpl;

    @BeforeAll
    public void init() {
        numericWordMatcherImpl = new NumericWordMatcherImpl();
    }

    @Test
    public void whenContainsWithNoMatching_thenReturnFalse() {
        assertFalse(numericWordMatcherImpl.contains("blah blah blah"));
    }

    @Test
    public void whenContainsWithDigits_thenReturnTrue() {
        String text = "Our sale revenue was $123456789 dollars last year, which beat 99% of our competitors. The YTD rise is 200%";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithDigitsAndDecimal_thenReturnTrue() {
        String text = "I made 99.88 dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithDigitsAndPercentage_thenReturnTrue() {
        String text = "Our sales revenue beat 99% of competitors";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithDigitsAndDollarSign_thenReturnTrue() {
        String text = "I made $99 dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithDigitsAndNegativeSign_thenReturnTrue() {
        String text = "I made -99 dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithDigitsAndPlusSignOnFront_thenReturnTrue() {
        String text = "I made +99 dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithDigitsAndFollowByPlusSign_thenReturnTrue() {
        String text = "I made 12345678+ dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithDigitsFollowByPunctuation_thenReturnTrue() {
        String text = "My score is 99, and yours is 0.";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithDigitsAndNumericWord_thenReturnTrue() {
        String text = "Our sales revenue beat 99% of competitors, and made one hundred millions.";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithNumericWord_thenReturnTrue() {
        String text = "I made one dollar last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithNumericWordsWithPlural_thenReturnNumHits() {
        String text = "I made two hundreds dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithConsecutiveNumericWords_thenReturnTrue() {
        String text = "I made two thousands one hundred thirty five dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void whenContainsWithMatching_thenReturnTrue() {
        String text = "Our sales revenue beat 99% of competitors";
        assertTrue(numericWordMatcherImpl.contains(text));
    }
}