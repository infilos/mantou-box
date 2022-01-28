package com.infilos.mantou.views.datetime.parser;

import com.infilos.utils.Datetimes;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DatetimeParser {

    static final Map<String, DateTimeFormatter> TimePatternMappings = new HashMap<>() {{
        put(Datetimes.Formats.AtSeconds, Datetimes.Patterns.AtSeconds);
        put(Datetimes.Formats.AtMinutes, Datetimes.Patterns.AtMinutes);
        put(Datetimes.Formats.AtHours, Datetimes.Patterns.AtHours);
        put(Datetimes.Formats.AtDays, Datetimes.Patterns.AtDays);
        put(Datetimes.Formats.AtMonths, Datetimes.Patterns.AtMonths);
        put(Datetimes.Formats.AtYears, Datetimes.Patterns.AtYears);
    }};

    static final List<String> TimePatterns = List.of(
        Datetimes.Formats.AtSeconds,
        Datetimes.Formats.AtMinutes,
        Datetimes.Formats.AtHours,
        Datetimes.Formats.AtDays,
        Datetimes.Formats.AtMonths,
        Datetimes.Formats.AtYears
    );

    static final String DefaultTimePattern = Datetimes.Formats.AtSeconds;
    static final int DefaultTimePatternIndex = TimePatterns.indexOf(Datetimes.Formats.AtSeconds);    
}
