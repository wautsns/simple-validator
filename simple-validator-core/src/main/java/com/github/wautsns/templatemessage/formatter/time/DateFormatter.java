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

import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

/**
 * Formatter for {@code Date} value.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@Data
@Accessors(chain = true)
public class DateFormatter implements Formatter<Date> {

    /** Default {@code DateFormatter}. */
    public static final DateFormatter DEFAULT = new DateFormatter();

    /** String format of {@code null}, default is {@code "null"}. */
    private String stringWhenNull = "null";
    /** Locale specified, default is {@code null}. */
    private Locale specifiedLocale = null;
    /** Date format style, default is {@link FormatStyle#MEDIUM}, not display if {@code null}. */
    private FormatStyle dateFormatStyle = FormatStyle.MEDIUM;
    /** Time format style, default is {@link FormatStyle#MEDIUM}, not display if {@code null}. */
    private FormatStyle timeFormatStyle = FormatStyle.MEDIUM;

    @Override
    public String format(Date value, Locale locale) {
        if (value == null) { return stringWhenNull; }
        if (specifiedLocale != null) { locale = specifiedLocale; }
        return TimeFormatterUtils.DateFormats
                .forDateAndTime(dateFormatStyle, timeFormatStyle, locale)
                .format(value);
    }

}
