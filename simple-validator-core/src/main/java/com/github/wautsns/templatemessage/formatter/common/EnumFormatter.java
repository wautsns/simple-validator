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
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    /** Default {@code EnumFormatter}. */
    public static final EnumFormatter DEFAULT = new EnumFormatter();

    private static final Map<Class<?>, String> STRING_FORMAT_PREFIX = new ConcurrentHashMap<>(32);

    /** String format of {@code null}, default is {@code "null"}. */
    private String stringFormatOfNull = "null";

    @Override
    public String format(Enum value, Locale locale) {
        return (value == null) ? stringFormatOfNull : (getStringFormatPrefix(value.getClass()) + value.name());
    }

    /**
     * Get string format prefix.
     *
     * @param enumClass enumeration class
     * @return string format prefix of the specified enumeration class
     */
    private String getStringFormatPrefix(Class<?> enumClass) {
        String prefix = STRING_FORMAT_PREFIX.get(enumClass);
        if (prefix != null) { return prefix; }
        Class<?> declaringClass = enumClass.getDeclaringClass();
        if (declaringClass == null) { return enumClass.getName() + "#"; }
        LinkedList<Class<?>> declaringClasses = new LinkedList<>();
        declaringClasses.add(enumClass);
        do {
            declaringClasses.addFirst(declaringClass);
            declaringClass = declaringClass.getDeclaringClass();
        } while (declaringClass != null);
        String first = declaringClasses.removeFirst().getName();
        prefix = declaringClasses.stream()
                .map(Class::getSimpleName)
                .collect(Collectors.joining("$", first + "$", "#"));
        String previousValue = STRING_FORMAT_PREFIX.putIfAbsent(enumClass, prefix);
        return (previousValue == null) ? prefix : previousValue;
    }

}
