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

import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Locale;

/**
 * Formatter for {@code Object} value.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@Setter
@Accessors(chain = true)
public class ObjectFormatter implements Formatter<Object> {

    /** default {@code ObjectFormatter} */
    public static final ObjectFormatter DEFAULT = new ObjectFormatter();

    private static final long serialVersionUID = -3276250483374591036L;

    /** string format for {@code null} */
    private @NonNull String stringFormatOfNull = "null";

    @Override
    public boolean appliesTo(Class<?> clazz) {
        return true;
    }

    @Override
    public String format(Object value, Locale locale) {
        if (value == null) {
            return stringFormatOfNull;
        }
        return value.toString();
    }

}
