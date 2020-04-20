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
import com.github.wautsns.templatemessage.formatter.time.util.TimeFormatterUtils;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Formatter for {@code LocalTimeFormatter} value.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@Data
@Accessors(chain = true)
public class LocalTimeFormatter implements Formatter<LocalTime> {

    /** default {@code LocalTimeFormatter} */
    public static final Formatter<LocalTime> DEFAULT = new LocalTimeFormatter();

    /** string format of {@code null}, default is {@code "null"} */
    private @NonNull String stringWhenNull = "null";
    /** locale specified when formatting, default is {@code null} */
    private Locale localeOfStringFormat = null;
    /** time format style, default is {@link FormatStyle#MEDIUM} */
    private @NonNull FormatStyle formatStyle = FormatStyle.MEDIUM;

    @Override
    public String format(LocalTime value, Locale locale) {
        if (value == null) { return stringWhenNull; }
        if (localeOfStringFormat != null) { locale = localeOfStringFormat; }
        return TimeFormatterUtils.DateTimeFormatters
                .forTime(formatStyle, locale)
                .format(value);
    }

}
