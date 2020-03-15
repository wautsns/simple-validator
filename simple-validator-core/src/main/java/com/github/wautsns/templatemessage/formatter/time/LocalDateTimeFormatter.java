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
package com.github.wautsns.templatemessage.formatter.time;

import com.github.wautsns.templatemessage.formatter.Formatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Formatter for {@code LocalDateTimeFormatter} value.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    /** default {@code LocalDateTimeFormatter} */
    public static final Formatter<LocalDateTime> DEFAULT = new LocalDateTimeFormatter();

    private static final long serialVersionUID = -7202909023391923980L;

    /** string format of {@code null}, default is {@code "null"} */
    private @NonNull String stringWhenNull = "null";
    /**
     * locale of string format, default is {@code null}
     *
     * <p>Set specified {@code Locale}, if you need to fix the locale of string format.
     */
    private Locale localeOfStringFormat = null;
    /**
     * date format style, default is {@link FormatStyle#MEDIUM}
     *
     * <p>Set to {@code null}, if you do not need to display the date.
     */
    private FormatStyle dateFormatStyle = FormatStyle.MEDIUM;
    /**
     * time format style, default is {@link FormatStyle#MEDIUM}
     *
     * <p>Set to {@code null}, if you do not need to display the time.
     */
    private FormatStyle timeFormatStyle = FormatStyle.MEDIUM;

    @Override
    public String format(LocalDateTime value, Locale locale) {
        if (value == null) { return stringWhenNull; }
        if (localeOfStringFormat != null) { locale = localeOfStringFormat; }
        return TimeFormatterUtils.DateTimeFormatters.forDateAndTime(dateFormatStyle, timeFormatStyle, locale)
                .format(value);
    }

}
