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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.Locale;

/**
 * Formatter for {@link Enum} value.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@SuppressWarnings("rawtypes")
public class EnumFormatter implements Formatter<Enum> {

    /** default {@code ObjectFormatter} */
    public static final EnumFormatter DEFAULT = new EnumFormatter();

    private static final long serialVersionUID = 7938927921714729160L;

    /** string format of {@code null}, default is {@code "null"} */
    private @NonNull String stringFormatOfNull = "null";
    /** whether to display class(all declaring class), default is {@code false} */
    private boolean displayClass = false;

    @Override
    public String format(Enum value, Locale locale) {
        if (value == null) { return stringFormatOfNull; }
        if (!displayClass) { return value.name(); }
        Class<?> clazz = value.getClass();
        Class<?> declaringClass = clazz.getDeclaringClass();
        if (declaringClass == null) {
            return clazz.getSimpleName() + '.' + value.name();
        } else {
            LinkedList<String> classList = new LinkedList<>();
            classList.add(clazz.getSimpleName());
            do {
                classList.addFirst(declaringClass.getSimpleName());
                declaringClass = declaringClass.getDeclaringClass();
            } while (declaringClass != null);
            return String.join("$", classList) + '.' + value.name();
        }
    }

}
