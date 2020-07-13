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

import java.util.Locale;
import java.util.Map;

/**
 * Formatter for {@code Map} value.
 *
 * @param <K> type of key
 * @param <V> type of value
 * @author wautsns
 * @since Mar 10, 2020
 */
@Data
@Accessors(chain = true)
public class MapFormatter<K, V> implements Formatter<Map<K, V>> {

    /** Default {@code MapFormatter}. */
    public static final Formatter<Map<Object, Object>> DEFAULT = new MapFormatter<>();

    /** String format of {@code null}, default is {@code "null"}. */
    private String stringFormatOfNull = "null";
    /** String format of empty array, default is {@code "[]"}. */
    private String stringFormatOfEmptyMap = "[]";
    /** Prefix of string format, default is {@code "["}. */
    private String prefix = "[";
    /** Suffix of string format, default is {@code "]"}. */
    private String suffix = "]";
    /** Prefix of string format of key, default is {@code ""}. */
    private String keyPrefix = "";
    /** Suffix of string format of key, default is {@code ""}. */
    private String keySuffix = "";
    /** Prefix of string format of value, default is {@code ""}. */
    private String valuePrefix = "";
    /** Suffix of string format of value, default is {@code ""}. */
    private String valueSuffix = "";
    /** Delimiter between string format of key and string format of value, default is {@code "="}. */
    private String keyValueDelimiter = "=";
    /** Delimiter of string format of entry, default is {@code ", "}. */
    private String delimiter = ", ";
    /** Formatter for key, default is {@link ObjectFormatter#DEFAULT}. */
    private Formatter<? super K> keyFormatter = ObjectFormatter.DEFAULT;
    /** Formatter for value, default is {@link ObjectFormatter#DEFAULT}. */
    private Formatter<? super V> valueFormatter = ObjectFormatter.DEFAULT;

    @Override
    public String format(Map<K, V> value, Locale locale) {
        if (value == null) { return stringFormatOfNull; }
        if (value.isEmpty()) { return stringFormatOfEmptyMap; }
        StringBuilder result = new StringBuilder();
        result.append(prefix);
        for (Map.Entry<K, V> entry : value.entrySet()) {
            String key = keyFormatter.format(entry.getKey(), locale);
            String val = valueFormatter.format(entry.getValue(), locale);
            result.append(keyPrefix).append(key).append(keySuffix);
            result.append(keyValueDelimiter);
            result.append(valuePrefix).append(val).append(valueSuffix);
            result.append(delimiter);
        }
        result.delete(result.length() - delimiter.length(), result.length());
        result.append(suffix);
        return result.toString();
    }

}
