package com.kuaidaoresume.common.matching;

import java.util.Collection;

public interface NumericWordMatcher extends ContainsMatcher {
    Collection<String> getMatches(String text);
}
