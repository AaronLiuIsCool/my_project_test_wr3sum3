package com.kuaidaoresume.common.matching;

import java.util.Collection;

public interface KeywordMatcher extends ContainsMatcher {
    Collection<String> getMatches(String text);
}
