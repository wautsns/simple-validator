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
package com.github.wautsns.templatemessage.formatter.multival;

import com.github.wautsns.templatemessage.formatter.Formatter;
import com.github.wautsns.templatemessage.formatter.common.ObjectFormatter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Iterator;
import java.util.Locale;

/**
 * Formatter for {@code Iterable} value.
 *
 * @param <E> type of element
 * @author wautsns
 * @since Mar 10, 2020
 */
@Data
@Accessors(chain = true)
public class IterableFormatter<E> implements Formatter<Iterable<E>> {

    /** Default {@code IterableFormatter}, e.g. [1, 2, 3]. */
    public static final Formatter<Iterable<Object>> DEFAULT = new IterableFormatter<>();

    /** String format of {@code null}, default is {@code "null"}. */
    private String stringFormatOfNull = "null";
    /** String format of empty array, default is {@code "[]"}. */
    private String stringFormatOfEmptyIterable = "[]";
    /** Prefix of string format, default is {@code "["}. */
    private String prefix = "[";
    /** Suffix of string format, default is {@code "]"}. */
    private String suffix = "]";
    /** Prefix of string format of element, default is {@code ""}. */
    private String elementPrefix = "";
    /** Suffix of string format of element, default is {@code ""}. */
    private String elementSuffix = "";
    /** Delimiter between string format of element, default is {@code ", "}. */
    private String elementDelimiter = ", ";
    /** Formatter for iterable element, default is {@link ObjectFormatter#DEFAULT}. */
    private Formatter<? super E> elementFormatter = ObjectFormatter.DEFAULT;

    @Override
    public String format(Iterable<E> value, Locale locale) {
        if (value == null) { return stringFormatOfNull; }
        Iterator<E> iterator = value.iterator();
        if (!iterator.hasNext()) { return stringFormatOfEmptyIterable; }
        StringBuilder result = new StringBuilder();
        result.append(prefix);
        do {
            String element = elementFormatter.format(iterator.next(), locale);
            result.append(elementPrefix).append(element).append(elementSuffix);
            result.append(elementDelimiter);
        } while (iterator.hasNext());
        result.delete(result.length() - elementDelimiter.length(), result.length());
        result.append(suffix);
        return result.toString();
    }

}
