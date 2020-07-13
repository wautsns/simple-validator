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

    /** Default {@code LocalTimeFormatter}. */
    public static final Formatter<LocalTime> DEFAULT = new LocalTimeFormatter();

    /** String format of {@code null}, default is {@code "null"}. */
    private String stringWhenNull = "null";
    /** Locale specified, default is {@code null}. */
    private Locale specifiedLocale = null;
    /** Time format style, default is {@link FormatStyle#MEDIUM}. */
    private FormatStyle formatStyle = FormatStyle.MEDIUM;

    @Override
    public String format(LocalTime value, Locale locale) {
        if (value == null) { return stringWhenNull; }
        if (specifiedLocale != null) { locale = specifiedLocale; }
        return TimeFormatterUtils.DateTimeFormatters
                .forTime(formatStyle, locale)
                .format(value);
    }

}
