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

import java.lang.reflect.Array;
import java.util.Locale;

/**
 * Formatter for array value.
 *
 * @param <A> type of array
 * @param <C> type of component
 * @author wautsns
 * @since Mar 10, 2020
 */
@Data
@Accessors(chain = true)
public class ArrayFormatter<A, C> implements Formatter<A> {

    /** Default {@code ArrayFormatter}. */
    public static final ArrayFormatter<Object, Object> DEFAULT = new ArrayFormatter<>();

    /** String format of {@code null}, default is {@code "null"}. */
    private String stringFormatOfNull = "null";
    /** String format of empty array, default is {@code "[]"}. */
    private String stringFormatOfEmptyArray = "[]";
    /** Prefix of string format, default is {@code "["}. */
    private String prefix = "[";
    /** Suffix of string format, default is {@code "]"}. */
    private String suffix = "]";
    /** Prefix of string format of component, default is {@code ""}. */
    private String componentPrefix = "";
    /** Suffix of string format of component, default is {@code ""}. */
    private String componentSuffix = "";
    /** Delimiter between string format of component, default is {@code ", "}. */
    private String delimiter = ", ";
    /** Formatter for component, default is {@link ObjectFormatter#DEFAULT}. */
    private Formatter<? super C> componentFormatter = ObjectFormatter.DEFAULT;

    @Override
    @SuppressWarnings("unchecked")
    public String format(A value, Locale locale) {
        if (value == null) { return stringFormatOfNull; }
        int length = Array.getLength(value);
        if (length == 0) { return stringFormatOfEmptyArray; }
        StringBuilder result = new StringBuilder();
        result.append(prefix);
        for (int i = 0; i < length; i++) {
            String component = componentFormatter.format((C) Array.get(value, i), locale);
            result.append(componentPrefix).append(component).append(componentSuffix);
            result.append(delimiter);
        }
        result.delete(result.length() - delimiter.length(), result.length());
        result.append(suffix);
        return result.toString();
    }

}
