package com.infilos.mantou.views.json.format;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.infilos.relax.Json;
import com.infilos.utils.Loggable;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.core.JsonToken.*;

public final class JsonService implements Loggable {
    private JsonService() {
    }

    private static final DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
    private static final DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("    ", DefaultIndenter.SYS_LF);
    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    static {
        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);
    }

    public static String clearSpaces(String json) {
        return Json.from(json).toString();
    }

    public static String format(String json) throws IOException {
        return Json.underMapper().writer(printer).writeValueAsString(Json.from(json).asJsonNode());
    }

    private static final String JSON_CURLY = "(?<JSONCURLY>\\{|\\})";
    private static final String JSON_PROPERTY = "(?<JSONPROPERTY>\\\".*\\\")\\s[^\\w]*:\\s*";
    private static final String JSON_VALUE = "(?<JSONVALUE>\\\".*\\\")";
    private static final String JSON_ARRAY = "\\[(?<JSONARRAY>.*)\\]";
    private static final String JSON_NUMBER = "(?<JSONNUMBER>\\d*.?\\d*)";
    private static final String JSON_BOOL = "(?<JSONBOOL>true|false)";

    private static final Pattern FINAL_REGEX = Pattern.compile(
        JSON_CURLY + "|"
            + JSON_PROPERTY + "|"
            + JSON_VALUE + "|"
            + JSON_ARRAY + "|"
            + JSON_BOOL + "|"
            + JSON_NUMBER
    );

    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = FINAL_REGEX.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
            = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass
                = matcher.group("JSONPROPERTY") != null ? "json_property"
                : matcher.group("JSONVALUE") != null ? "json_value"
                : matcher.group("JSONARRAY") != null ? "json_array"
                : matcher.group("JSONCURLY") != null ? "json_curly"
                : matcher.group("JSONBOOL") != null ? "json_bool"
                : matcher.group("JSONNUMBER") != null ? "json_number"
                : null;
            /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);

        return spansBuilder.create();
    }
    
    public static StyleSpans<Collection<String>> highlighting(String text) {
        List<MatchSegment> matchSegments = new ArrayList<>();
        
        try {
            JsonParser parser = JSON_FACTORY.createParser(text);
            while (!parser.isClosed()) {
                JsonToken jsonToken = parser.nextToken();
                int start = (int) parser.getTokenLocation().getCharOffset();
                int end = start + parser.getTextLength();

                // Because getTextLength() does contain the surrounding ""
                if (jsonToken == JsonToken.VALUE_STRING || jsonToken == JsonToken.FIELD_NAME) {
                    end += 2;
                }

                String styleClass = styleClassOf(jsonToken);
                if (!styleClass.isEmpty()) {
                    MatchSegment segment = new MatchSegment(styleClass, start, end);
                    matchSegments.add(segment);
                }
            }
        } catch (IOException ignored){
        }
        
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        int lastPos = 0;
        for (MatchSegment segment : matchSegments) {
            // Fill the gaps, since Style Spans need to be contiguous.
            if (segment.start > lastPos) {
                int length = segment.start - lastPos;
                spansBuilder.add(Collections.emptyList(), length);
            }

            int length = segment.end - segment.start;
            spansBuilder.add(Collections.singleton(segment.style), length);
            lastPos = segment.end;
        }

        if (lastPos == 0) {
            spansBuilder.add(Collections.emptyList(), text.length());
        }
        
        return spansBuilder.create();
    }
    
    private static String styleClassOf(JsonToken token) {
        if (Objects.isNull(token)) {
            return "";
        } else if (token == FIELD_NAME) {
            return "json_property";
        } else if (token == VALUE_STRING) {
            return "json_value";
        } else if (token == VALUE_NUMBER_INT || token == VALUE_NUMBER_FLOAT) {
            return "json_number";
        } else if (token == VALUE_TRUE || token == VALUE_FALSE) {
            return "json_bool";
        } else if (token == START_OBJECT || token == END_OBJECT) {
            return "json_curly";
        } else if (token == START_ARRAY || token == END_ARRAY) {
            return "json_array";
        } else if (token == VALUE_NULL) {
            return "json_null";
        } else {
            return "";
        }
    }

    private record MatchSegment(String style, int start, int end) implements Comparable<MatchSegment> {
        @Override
        public int compareTo(MatchSegment that) {
            return Integer.compare(start, that.start);
        }
    }
}

