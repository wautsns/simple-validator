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
package com.github.wautsns.templatemessage.formatter;

import com.github.wautsns.templatemessage.formatter.multival.ArrayFormatter;
import com.github.wautsns.templatemessage.formatter.multival.IterableFormatter;
import com.github.wautsns.templatemessage.formatter.multival.MapFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Locale;
import java.util.Map;

/**
 * Formatter for {@code Object} value.
 *
 * <ul>
 * <li>If value is {@code null}, see {@linkplain #getStringFormatOfNull()}.</li>
 * <li>If value is array, {@link ArrayFormatter#DEFAULT} will be used.</li>
 * <li>If value instance of {@code Iterable}, {@link IterableFormatter#DEFAULT} will be used.</li>
 * <li>If value instance of {@code Map}, {@link MapFormatter#DEFAULT} will be used.</li>
 * <li>Otherwise {@code value.toString()}.</li>
 * </ul>
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class ObjectFormatter implements Formatter<Object> {

    /** default {@code ObjectFormatter} */
    public static final ObjectFormatter DEFAULT = new ObjectFormatter();

    private static final long serialVersionUID = -3276250483374591036L;

    /** string format for {@code null}, default is {@code "null"} */
    private @NonNull
    String stringFormatOfNull = "null";

    @Override
    @SuppressWarnings("unchecked")
    public String format(Object value, Locale locale) {
        if (value == null) {
            return stringFormatOfNull;
        } else if (value instanceof CharSequence) {
            return value.toString();
        } else if (value.getClass().isArray()) {
            return ArrayFormatter.DEFAULT.format(value, locale);
        } else if (value instanceof Iterable) {
            return IterableFormatter.DEFAULT.format((Iterable<Object>) value, locale);
        } else if (value instanceof Map) {
            return MapFormatter.DEFAULT.format((Map<Object, Object>) value, locale);
        } else {
            return value.toString();
        }
    }

}
