/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wautsns.templatemessage.formatter.time.util;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utils for date and time.
 *
 * @author wautsns
 * @since Mar 12, 2020
 */
@UtilityClass
public class TimeFormatterUtils {

    /** Utils for {@link DateFormat}. */
    @UtilityClass
    public static class DateFormats {

        /**
         * Get {@link DateFormat} for displaying date and time.
         *
         * <p>If you do not need to display the date/time, give {@code null}.
         *
         * @param dateFormatStyle formatStyle for date part
         * @param timeFormatStyle formatStyle for time part
         * @param locale locale
         * @return {@link DateFormat}
         */
        public static DateFormat forDateAndTime(
                FormatStyle dateFormatStyle, FormatStyle timeFormatStyle, Locale locale) {
            if (dateFormatStyle == null) { return forTime(timeFormatStyle, locale); }
            if (timeFormatStyle == null) { return forDate(dateFormatStyle, locale); }
            int codeForDate = getDateFormatStyleCode(dateFormatStyle);
            int codeForTime = getDateFormatStyleCode(timeFormatStyle);
            return DateFormat.getDateTimeInstance(codeForDate, codeForTime, Objects.requireNonNull(locale));
        }

        /**
         * Get {@link DateFormat} for displaying date.
         *
         * @param formatStyle formatStyle for date part
         * @param locale locale
         * @return {@link DateFormat} for displaying date
         */
        public static DateFormat forDate(FormatStyle formatStyle, Locale locale) {
            int code = getDateFormatStyleCode(formatStyle);
            return DateFormat.getDateInstance(code, Objects.requireNonNull(locale));
        }

        /**
         * Get {@link DateFormat} for displaying time.
         *
         * @param formatStyle formatStyle for time part
         * @param locale locale
         * @return {@link DateFormat} for displaying time
         */
        public static DateFormat forTime(FormatStyle formatStyle, Locale locale) {
            int code = getDateFormatStyleCode(formatStyle);
            return DateFormat.getTimeInstance(code, Objects.requireNonNull(locale));
        }

        /**
         * Get code of {@code DateFormat} by {@code FormatStyle}.
         *
         * @param formatStyle format style
         * @return code of {@code DateFormat} associated with the format style
         */
        private static int getDateFormatStyleCode(FormatStyle formatStyle) {
            switch (Objects.requireNonNull(formatStyle)) {
                case LONG:
                    return DateFormat.LONG;
                case MEDIUM:
                    return DateFormat.MEDIUM;
                case FULL:
                    return DateFormat.FULL;
                case SHORT:
                    return DateFormat.SHORT;
                default:
                    throw new IllegalStateException();
            }
        }

    }

    /** Utils for {@link DateTimeFormatter}. */
    @UtilityClass
    public static class DateTimeFormatters {

        /** Cache of {@code DateTimeFormatter} for displaying date only. */
        private static final EnumMap<FormatStyle, Map<Locale, DateTimeFormatter>> CACHE_ONLY_DATE =
                new EnumMap<>(FormatStyle.class);
        /** Cache of {@code DateTimeFormatter} for displaying time only. */
        private static final EnumMap<FormatStyle, Map<Locale, DateTimeFormatter>> CACHE_ONLY_TIME =
                new EnumMap<>(FormatStyle.class);
        /** Cache of {@code DateTimeFormatter} for displaying date and time. */
        private static final EnumMap<FormatStyle, EnumMap<FormatStyle, Map<Locale, DateTimeFormatter>>> CACHE_DATE_TIME =
                new EnumMap<>(FormatStyle.class);

        /**
         * Get {@link DateTimeFormatter} for displaying date and time.
         *
         * <p>If you do not need to display the date/time, give {@code null}.
         *
         * @param dateFormatStyle formatStyle for date part
         * @param timeFormatStyle formatStyle for time part
         * @param locale locale
         * @return {@link DateTimeFormatter}
         */
        public static DateTimeFormatter forDateAndTime(
                FormatStyle dateFormatStyle, FormatStyle timeFormatStyle, Locale locale) {
            if (dateFormatStyle == null) { return forTime(timeFormatStyle, locale); }
            if (timeFormatStyle == null) { return forDate(dateFormatStyle, locale); }
            return CACHE_DATE_TIME
                    .computeIfAbsent(dateFormatStyle, i -> new EnumMap<>(FormatStyle.class))
                    .computeIfAbsent(timeFormatStyle, i -> new ConcurrentHashMap<>())
                    .computeIfAbsent(
                            Objects.requireNonNull(locale),
                            loc -> DateTimeFormatter
                                    .ofLocalizedDateTime(dateFormatStyle, timeFormatStyle)
                                    .withLocale(loc));
        }

        /**
         * Get {@link DateTimeFormatter} for displaying date.
         *
         * @param formatStyle formatStyle for date part
         * @param locale locale
         * @return {@link DateTimeFormatter} for displaying date
         */
        public static DateTimeFormatter forDate(FormatStyle formatStyle, Locale locale) {
            return CACHE_ONLY_DATE
                    .computeIfAbsent(Objects.requireNonNull(formatStyle), i -> new ConcurrentHashMap<>())
                    .computeIfAbsent(
                            Objects.requireNonNull(locale),
                            loc -> DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(loc));
        }

        /**
         * Get {@link DateTimeFormatter} for displaying time.
         *
         * @param formatStyle formatStyle for time part
         * @param locale locale
         * @return {@link DateTimeFormatter} for displaying time
         */
        public static DateTimeFormatter forTime(FormatStyle formatStyle, Locale locale) {
            return CACHE_ONLY_TIME
                    .computeIfAbsent(Objects.requireNonNull(formatStyle), i -> new ConcurrentHashMap<>())
                    .computeIfAbsent(
                            Objects.requireNonNull(locale),
                            loc -> DateTimeFormatter.ofLocalizedTime(formatStyle).withLocale(loc));
        }

    }

}
