package com.infilos.mantou.views.json.format;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindState {
    private final String pattern;
    private final String content;
    private final List<SearchSpan> found;
    private int current;
    private int total;

    public FindState(String pattern, String content) {
        this.pattern = pattern;
        this.content = content;
        this.found = new ArrayList<>();
        this.current = 0;
        this.total = 0;
        setAll();
    }

    private void setAll() {
        if (pattern.isEmpty()) {
            return;
        }
        Matcher m = Pattern.compile(pattern).matcher(content);
        while (m.find()) {
            found.add(new SearchSpan(m.start(), m.start() + pattern.length()));
            total++;
        }
    }

    public Optional<SearchSpan> next() {
        if (total == 0) return Optional.empty();
        current = current + 1 > total ? 1 : current + 1;
        return Optional.of(found.get(current - 1));
    }

    public Optional<SearchSpan> prev() {
        if (total == 0) return Optional.empty();
        current = current - 1 > 0 ? current - 1 % total : total;
        return Optional.of(found.get(current - 1));
    }

    public boolean isValid(String text, String target) {
        return pattern.equals(text) && this.content.equals(target);
    }

    @Override
    public String toString() {
        return current + "/" + total;
    }

    public record SearchSpan(int from, int to) {
        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }
    }
}
