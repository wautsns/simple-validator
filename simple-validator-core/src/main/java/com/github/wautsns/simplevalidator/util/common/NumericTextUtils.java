/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.wautsns.simplevalidator.util.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Numeric text utils.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class NumericTextUtils {

    private static final Map<Class<?>, Function<String, ?>> NUMERIC_TEXT_PARSERS;

    static {
        NUMERIC_TEXT_PARSERS = new HashMap<>();
        addParser(BigDecimal.class, BigDecimal::new);
        addParser(BigInteger.class, BigInteger::new);
        addParser(Double.class, Double::valueOf);
        addParser(Float.class, Float::valueOf);
        addParser(Long.class, Long::valueOf);
        addParser(Integer.class, Integer::valueOf);
        addParser(Short.class, Short::valueOf);
        addParser(Byte.class, Byte::valueOf);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number & Comparable<T>> T parse(String text, Class<T> clazz) {
        return (T) NUMERIC_TEXT_PARSERS.get(clazz).apply(text);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends Number & Comparable<T>> void addParser(Class<T> clazz, Function<String, T> parser) {
        NUMERIC_TEXT_PARSERS.put(clazz, (Function) parser);
    }

    private NumericTextUtils() {}

}
