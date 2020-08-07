package com.kuaidaoresume.common.matching;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NameMatcher implements ContainsMatcher {

    private final Set<String> names;

    public NameMatcher(Collection<String> names) {
        this.names = new HashSet<>(names);
    }

    @Override
    public boolean contains(String text) {
        return names.contains(text);
    }
}
