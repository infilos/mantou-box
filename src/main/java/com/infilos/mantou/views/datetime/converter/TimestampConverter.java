package com.infilos.mantou.views.datetime.converter;

import com.infilos.utils.Datetimes;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TimestampConverter {

    static final List<String> AllTimeZones = new ArrayList<>(ZoneId.getAvailableZoneIds());
    static final String LocalTimeZone = ZoneId.systemDefault().getId();
    
    static final List<String> AllTimeZoneOffsets = buildTimeZoneOffsets();
    static final String LocalZoneOffset = buildZoneOffset(TimeZone.getDefault().getID(), LocalDateTime.now());
    static final int LocalZoneOffsetIndex = AllTimeZoneOffsets.indexOf(LocalZoneOffset);

    static final Map<String, DateTimeFormatter> TimePatternMappings = new HashMap<>() {{
        put(Datetimes.Formats.AtSeconds, Datetimes.Patterns.AtSeconds);
        put(Datetimes.Formats.AtMinutes, Datetimes.Patterns.AtMinutes);
        put(Datetimes.Formats.AtHours, Datetimes.Patterns.AtHours);
        put(Datetimes.Formats.AtDays, Datetimes.Patterns.AtDays);
        put(Datetimes.Formats.AtMonths, Datetimes.Patterns.AtMonths);
        put(Datetimes.Formats.AtYears, Datetimes.Patterns.AtYears);
        put("yyyy-MM-dd[ HH:mm:ss]", new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter()
        );
        put("yyyy-MM[-dd HH:mm:ss]", new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM[-dd HH:mm:ss]")
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter());
        put("yyyy[-MM-dd HH:mm:ss]", new DateTimeFormatterBuilder()
            .appendPattern("yyyy[-MM-dd HH:mm:ss]")
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter()
        );
    }};

    static final String DefaultTimePattern = Datetimes.Formats.AtSeconds;

    static final List<String> TimePatterns = List.of(
        Datetimes.Formats.AtSeconds,
        Datetimes.Formats.AtMinutes,
        Datetimes.Formats.AtHours,
        Datetimes.Formats.AtDays,
        Datetimes.Formats.AtMonths,
        Datetimes.Formats.AtYears,
        "yyyy-MM-dd[ HH:mm:ss]",
        "yyyy-MM[-dd HH:mm:ss]",
        "yyyy[-MM-dd HH:mm:ss]"
    );

    static final List<String> TimeModes = List.of(
        "Unixtime(10)",
        "Millisecond(13)",
        "Microsecond(16)",
        "Nanosecond(19)"
    );

    static final Map<String, Integer> TimeModeLengths = new HashMap<>() {{
        put("Unixtime(10)", 10);
        put("Millisecond(13)", 13);
        put("Microsecond(16)", 16);
        put("Nanosecond(19)", 19);
    }};

    static final String DefaultTimeMode = "Millisecond(13)";
    static final int DefaultTimeModeIndex = TimeModes.indexOf(DefaultTimeMode);

    static Instant currentInstant(String zone) {
        return Clock.system(ZoneId.of(zone)).instant();
    }

    static long buildTimestamp(Instant instant, String mode) {
        return switch (mode) {
            case "Unixtime(10)" -> instant.getEpochSecond();
            case "Millisecond(13)" -> instant.toEpochMilli();
            case "Microsecond(16)" -> TimeUnit.SECONDS.toMicros(instant.getEpochSecond()) + TimeUnit.NANOSECONDS.toMicros(instant.getNano());
            case "Nanosecond(19)" -> TimeUnit.SECONDS.toNanos(instant.getEpochSecond()) + instant.getNano();
            default -> 0L;
        };
    }

    static String buildDatetime(Instant instant, String zone, String pattern) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneId.of(zone));
        TimePatternMappings.computeIfAbsent(pattern.trim(), k -> DateTimeFormatter.ofPattern(pattern));

        return dateTime.format(TimePatternMappings.get(pattern));
    }

    static Instant buildInstant(long timestamp, String timeMode) {
        return switch (timeMode) {
            case "Unixtime(10)" -> Instant.ofEpochSecond(timestamp);
            case "Millisecond(13)" -> Instant.ofEpochMilli(timestamp);
            case "Microsecond(16)" -> Instant.ofEpochSecond(TimeUnit.MICROSECONDS.toSeconds(timestamp), TimeUnit.MICROSECONDS.toNanos(Math.floorMod(timestamp, TimeUnit.SECONDS.toMicros(1))));
            case "Nanosecond(19)" -> Instant.ofEpochSecond(0L, timestamp);
            default -> throw new RuntimeException("Will never happen!");
        };
    }

    private static List<String> buildTimeZoneOffsets() {
        List<String> timeZoneWithOffsets= new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        
        for(String zone:AllTimeZones) {
            timeZoneWithOffsets.add(buildZoneOffset(zone, localDateTime));
        }
        
        return timeZoneWithOffsets;
    }
    
    private static String buildZoneOffset(String zone, LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.of(zone);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        ZoneOffset zoneOffset = zonedDateTime.getOffset();
        String offset = zoneOffset.getId().replaceAll("Z", "+00:00");
        
        return zone + "(" + offset + ")";
    }
}
