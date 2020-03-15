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
import com.github.wautsns.templatemessage.formatter.ObjectFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
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
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class IterableFormatter<E> implements Formatter<Iterable<E>> {

    /** the default {@code IterableFormatter}, eg. [1, 2, 3] */
    public static final Formatter<Iterable<Object>> DEFAULT = new IterableFormatter<>();

    private static final long serialVersionUID = 9215356454938186580L;

    /** string format of {@code null}, default is {@code "null"} */
    private @NonNull String stringFormatOfNull = "null";
    /** string format of empty array, default is {@code "[]"} */
    private @NonNull String stringFormatOfEmptyIterable = "[]";
    /** prefix of string format, default is {@code "["} */
    private @NonNull String prefix = "[";
    /** suffix of string format, default is {@code "]"} */
    private @NonNull String suffix = "]";
    /** prefix of string format of element, default is {@code ""} */
    private @NonNull String elementPrefix = "";
    /** suffix of string format of element, default is {@code ""} */
    private @NonNull String elementSuffix = "";
    /** delimiter between string format of element, default is {@code ", "} */
    private @NonNull String elementDelimiter = ", ";
    /** formatter for iterable element, default is {@link ObjectFormatter#DEFAULT} */
    private @NonNull Formatter<? super E> elementFormatter = ObjectFormatter.DEFAULT;

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

    /**
     * Set prefix and suffix.
     *
     * @param prefix prefix of string formatter
     * @param suffix suffix of string formatter
     * @return self reference
     */
    public IterableFormatter<E> setPrefixAndSuffix(String prefix, String suffix) {
        setPrefix(prefix);
        setSuffix(suffix);
        return this;
    }

    /**
     * Set prefix of string format of element and suffix of string format of element.
     *
     * @param prefix prefix of string format of element
     * @param suffix suffix of string format of element
     * @return self reference
     */
    public IterableFormatter<E> setElementPrefixAndSuffix(String prefix, String suffix) {
        setElementPrefix(prefix);
        setElementSuffix(suffix);
        return this;
    }

}
