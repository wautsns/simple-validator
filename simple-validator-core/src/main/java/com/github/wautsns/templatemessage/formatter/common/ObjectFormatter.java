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
package com.github.wautsns.templatemessage.formatter.common;

import com.github.wautsns.templatemessage.formatter.Formatter;
import com.github.wautsns.templatemessage.formatter.multival.ArrayFormatter;
import com.github.wautsns.templatemessage.formatter.multival.IterableFormatter;
import com.github.wautsns.templatemessage.formatter.multival.MapFormatter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Locale;
import java.util.Map;

/**
 * Formatter for {@code Object} value.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@Data
@Accessors(chain = true)
public class ObjectFormatter implements Formatter<Object> {

    /** Default {@code ObjectFormatter}. */
    public static final ObjectFormatter DEFAULT = new ObjectFormatter();

    /** String format of {@code null}, default is {@code "null"}. */
    private String stringFormatOfNull = "null";

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
        } else if (value instanceof Enum) {
            return EnumFormatter.DEFAULT.format((Enum<?>) value, locale);
        } else {
            return value.toString();
        }
    }

}
