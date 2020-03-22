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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
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
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class MapFormatter<K, V> implements Formatter<Map<K, V>> {

    /** the default {@code MapFormatter}, eg. {a=1, b=2, c=3} */
    public static final Formatter<Map<Object, Object>> DEFAULT = new MapFormatter<>();

    /** serialVersionUID */
    private static final long serialVersionUID = -984612299189087446L;

    /** string format of {@code null}, default is {@code "null"} */
    private @NonNull String stringFormatOfNull = "null";
    /** string format of empty array, default is {@code "[]"} */
    private @NonNull String stringFormatOfEmptyMap = "[]";
    /** prefix of string format, default is {@code "["} */
    private @NonNull String prefix = "[";
    /** suffix of string format, default is {@code "]"} */
    private @NonNull String suffix = "]";
    /** prefix of string format of key, default is {@code ""} */
    private @NonNull String keyPrefix = "";
    /** suffix of string format of key, default is {@code ""} */
    private @NonNull String keySuffix = "";
    /** prefix of string format of value, default is {@code ""} */
    private @NonNull String valuePrefix = "";
    /** suffix of string format of value, default is {@code ""} */
    private @NonNull String valueSuffix = "";
    /** delimiter between string format of key and string format of value, default is {@code "="} */
    private @NonNull String keyValueDelimiter = "=";
    /** delimiter of string format of entry, default is {@code ", "} */
    private @NonNull String delimiter = ", ";
    /** formatter for key, default is {@link ObjectFormatter#DEFAULT} */
    private @NonNull Formatter<? super K> keyFormatter = ObjectFormatter.DEFAULT;
    /** formatter for value, default is {@link ObjectFormatter#DEFAULT} */
    private @NonNull Formatter<? super V> valueFormatter = ObjectFormatter.DEFAULT;

    /**
     * Set prefix and suffix.
     *
     * @param prefix prefix of string formatter
     * @param suffix suffix of string formatter
     * @return self reference
     */
    public MapFormatter<K, V> setPrefixAndSuffix(String prefix, String suffix) {
        setPrefix(prefix);
        setSuffix(suffix);
        return this;
    }

    /**
     * Set prefix of string format of key and suffix of string format of key.
     *
     * @param prefix prefix of string format of key
     * @param suffix suffix of string format of key
     * @return self reference
     */
    public MapFormatter<K, V> setKeyPrefixAndSuffix(String prefix, String suffix) {
        setKeyPrefix(prefix);
        setKeySuffix(suffix);
        return this;
    }

    /**
     * Set prefix of string format of value and suffix of string format of value.
     *
     * @param prefix prefix of string format of value
     * @param suffix suffix of string format of value
     * @return self reference
     */
    public MapFormatter<K, V> setValuePrefixAndSuffix(String prefix, String suffix) {
        setValuePrefix(prefix);
        setValueSuffix(suffix);
        return this;
    }

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
