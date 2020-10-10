package com.kuaidaoresume.common.matching;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumericWordMatcherImplTest {
    private NumericWordMatcherImpl numericWordMatcherImpl;

    @BeforeAll
    public void init() {
        numericWordMatcherImpl = new NumericWordMatcherImpl(true);
    }

    @Test
    public void whenContainsWithNoMatching_thenReturnFalse() {
        assertFalse(numericWordMatcherImpl.contains("blah blah blah"));
    }

    @Test
    public void test_getMatchesWithNoMatching() {
        assertThat(numericWordMatcherImpl.getMatches("blah blah blah"), is(Sets.newHashSet()));
    }

    @Test
    public void whenContainsWithDigits_thenReturnTrue() {
        String text = "Our sale revenue was $123456789 dollars last year, which beat 99% of our competitors. The YTD rise is 200%";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigits() {
        String text = "Our sale revenue was $123456789 dollars last year, which beat 99% of our competitors. The YTD rise is 200%";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("$123456789", "99%", "200%")));
    }

    @Test
    public void whenContainsWithDigitsChinese_thenReturnTrue() {
        String text = "我们去年的收入是$123456789元，击败了99%的竞争对手。年增长率是200%。";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsChinese() {
        String text = "我们去年的收入是$123456789元，击败了99%的竞争对手。年增长率是200%。";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("$123456789", "99%", "200%")));
    }

    @Test
    public void whenContainsWithDigitsAndDecimal_thenReturnTrue() {
        String text = "I made 99.88 dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndDecimal() {
        String text = "I made 99.88 dollars last year";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("99.88")));
    }

    @Test
    public void whenContainsWithDigitsAndDecimalChinese_thenReturnTrue() {
        String text = "去年我赚了99.88元";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndDecimalChinese() {
        String text = "去年我赚了99.88元。";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("99.88")));
    }

    @Test
    public void whenContainsWithDigitsAndPercentage_thenReturnTrue() {
        String text = "Our sales revenue beat 99% of competitors";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndPercentage() {
        String text = "Our sales revenue beat 99% of competitors";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("99%")));
    }

    @Test
    public void whenContainsWithDigitsAndPercentageChinese_thenReturnTrue() {
        String text = "我们的销售击败了99%的竞争对手";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndPercentageChinese() {
        String text = "我们的销售击败了99%的竞争对手";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("99%")));
    }

    @Test
    public void whenContainsWithDigitsAndDollarSign_thenReturnTrue() {
        String text = "I made $99 dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndDollarSign() {
        String text = "I made $99 dollars last year";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("$99")));
    }

    @Test
    public void whenContainsWithDigitsAndDollarSignChinese_thenReturnTrue() {
        String text = "去年我赚了¥99元";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndDollarSignChinese() {
        String text = "去年我赚了¥99元";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("¥99")));
    }

    @Test
    public void whenContainsWithDigitsAndNegativeSign_thenReturnTrue() {
        String text = "I made -99 dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesContainsWithDigitsAndNegativeSign() {
        String text = "I made -99 dollars last year";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("-99")));
    }

    @Test
    public void whenContainsWithDigitsAndNegativeSignChinese_thenReturnTrue() {
        String text = "去年我赚了-99元";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesContainsWithDigitsAndNegativeSignChinese() {
        String text = "去年我赚了-99元";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("-99")));
    }

    @Test
    public void whenContainsWithDigitsAndPlusSignOnFront_thenReturnTrue() {
        String text = "I made +99 dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndPlusSignOnFront() {
        String text = "I made +99 dollars last year";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("+99")));
    }

    @Test
    public void whenContainsWithDigitsAndPlusSignOnFrontChinese_thenReturnTrue() {
        String text = "去年我赚了+99元";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndPlusSignOnFrontChinese() {
        String text = "去年我赚了+99元";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("+99")));
    }

    @Test
    public void whenContainsWithDigitsAndFollowByPlusSign_thenReturnTrue() {
        String text = "I made 12345678+ dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndFollowByPlusSign() {
        String text = "I made 12345678+ dollars last year";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("12345678+")));
    }

    @Test
    public void whenContainsWithDigitsAndFollowByPlusSignChinese_thenReturnTrue() {
        String text = "我去年赚了12345678+元";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndFollowByPlusSignChinese() {
        String text = "我去年赚了12345678+元";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("12345678+")));
    }

    @Test
    public void whenContainsWithDigitsFollowByPunctuation_thenReturnTrue() {
        String text = "My score is 99, and yours is 0.";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsFollowByPunctuation() {
        String text = "My score is 99, and yours is 0.";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("99", "0")));
    }

    @Test
    public void whenContainsWithDigitsFollowByPunctuationChinese_thenReturnTrue() {
        String text = "我的分数是99，你的是0。";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsFollowByPunctuationChinese() {
        String text = "我的分数是99，你的是0。";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("99", "0")));
    }

    @Test
    public void whenContainsWithDigitsAndNumericWord_thenReturnTrue() {
        String text = "Our sales revenue beat 99% of competitors, and made one hundred millions.";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Disabled // TODO: SL More sophisticated matching algorithm is required to handle this case
    @Test
    public void test_getMatchesWithDigitsAndNumericWord() {
        String text = "Our sales revenue beat 99% of competitors, and made one hundred millions.";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("99%", "one hundred millions")));
    }

    @Test
    public void whenContainsWithNumericWord_thenReturnTrue() {
        String text = "I made one dollar last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithNumericWord() {
        String text = "I made one dollar last year";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("one")));
    }

    @Test
    public void whenContainsWithNumericWordsWithPlural_thenReturnNumHits() {
        String text = "I made two hundreds dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Disabled // TODO: SL More sophisticated matching algorithm is required to handle this case
    @Test
    public void test_getMatchesWithNumericWordsWithPlural() {
        String text = "I made two hundreds dollars last year";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("two hundreds")));
    }

    @Test
    public void whenContainsWithConsecutiveNumericWords_thenReturnTrue() {
        String text = "I made two thousands one hundred thirty five dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Disabled // TODO: SL More sophisticated matching algorithm is required to handle this case
    @Test
    public void test_getMatchesWithConsecutiveNumericWords() {
        String text = "I made two thousands one hundred thirty five dollars last year";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("two thousands one hundred thirty five")));
    }

    @Test
    public void whenContainsWithDigitsAndCommas_thenReturnTrue() {
        String text = "I made $12,345,678 dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndCommas() {
        String text = "I made $12,345,678 dollars last year";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("$12,345,678")));
    }

    @Test
    public void whenContainsWithDigitsAndCommasAndDecimal_thenReturnTrue() {
        String text = "I made $12,345,678.99 dollars last year";
        assertTrue(numericWordMatcherImpl.contains(text));
    }

    @Test
    public void test_getMatchesWithDigitsAndCommasAndDecimal() {
        String text = "I made $12,345,678.99 dollars last year";
        assertThat(numericWordMatcherImpl.getMatches(text), is(Sets.newHashSet("$12,345,678.99")));
    }
}
