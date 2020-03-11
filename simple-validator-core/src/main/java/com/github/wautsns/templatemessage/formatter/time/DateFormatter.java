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
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.text.DateFormat;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

/**
 * Formatter for {@code Date} value.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@Setter
@Accessors(chain = true)
public class DateFormatter implements Formatter<Date> {

    /** default {@code DateFormatter} */
    public static final DateFormatter DEFAULT = new DateFormatter();

    private static final long serialVersionUID = 3675512503772257828L;

    /** string format of {@code null}, default is {@code "null"} */
    private @NonNull String stringWhenNull = "null";
    /**
     * locale of string format, default is {@code null}
     *
     * <p>Set a {@code Locale}, if you need to fix the locale of string format.
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
    public boolean appliesTo(Class<?> clazz) {
        return Date.class.isAssignableFrom(clazz);
    }

    @Override
    public String format(Date value, Locale locale) {
        if (value == null) { return stringWhenNull; }
        if (localeOfStringFormat != null) { locale = localeOfStringFormat; }
        DateFormat dateFormat;
        if (timeFormatStyle == null) {
            int dateStyleCode = getDateFormatStyleCode(dateFormatStyle);
            dateFormat = DateFormat.getDateInstance(dateStyleCode, locale);
        } else if (dateFormatStyle == null) {
            int timeStyleCode = getDateFormatStyleCode(timeFormatStyle);
            dateFormat = DateFormat.getTimeInstance(timeStyleCode, locale);
        } else {
            int dateStyleCode = getDateFormatStyleCode(dateFormatStyle);
            int timeStyleCode = getDateFormatStyleCode(timeFormatStyle);
            dateFormat = DateFormat.getDateTimeInstance(dateStyleCode, timeStyleCode, locale);
        }
        return dateFormat.format(value);
    }

    /**
     * Get the code of {@code DateFormat} from {@code FormatStyle}.
     *
     * @param formatStyle a format style
     * @return the code of {@code DateFormat} associated with the format style
     */
    private static int getDateFormatStyleCode(FormatStyle formatStyle) {
        switch (formatStyle) {
            case LONG:
                return DateFormat.LONG;
            case MEDIUM:
                return DateFormat.MEDIUM;
            case FULL:
                return DateFormat.FULL;
            case SHORT:
                return DateFormat.SHORT;
            default:
                throw new IllegalArgumentException();
        }
    }

}
