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
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.Locale;

/**
 * Formatter for {@link Enum} value.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Data
@Accessors(chain = true)
@SuppressWarnings("rawtypes")
public class EnumFormatter implements Formatter<Enum> {

    /** default {@code EnumFormatter} */
    public static final EnumFormatter DEFAULT = new EnumFormatter();

    /** string format of {@code null}, default is {@code "null"} */
    private @NonNull String stringFormatOfNull = "null";
    /** whether to display declaring class(es), default is {@code false} */
    private boolean displayDeclaringClass = false;

    @Override
    public String format(Enum value, Locale locale) {
        if (value == null) { return stringFormatOfNull; }
        if (!displayDeclaringClass) { return value.name(); }
        Class<?> enumClass = value.getClass();
        Class<?> declaringClass = enumClass.getDeclaringClass();
        if (declaringClass == null) {
            return enumClass.getSimpleName() + "." + value.name();
        } else {
            LinkedList<String> declaringClasses = new LinkedList<>();
            declaringClasses.add(enumClass.getSimpleName());
            do {
                declaringClasses.addFirst(declaringClass.getSimpleName());
                declaringClass = declaringClass.getDeclaringClass();
            } while (declaringClass != null);
            return String.join("$", declaringClasses) + "." + value.name();
        }
    }

}
