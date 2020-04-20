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

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Numeric text parser.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@UtilityClass
public class NumericTextParser {

    /**
     * Parse text into a numeric value of the specified type.
     *
     * @param type numeric value type
     * @param text text
     * @param <T> type of numeric value
     * @return numeric value
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number & Comparable<T>> T parse(Class<T> type, String text) {
        return (T) NUMERIC_TEXT_PARSER_MAP.get(type).apply(text);
    }

    /**
     * Add numeric text parser.
     *
     * <ul>
     * the types supported by default are as follows:
     * <li>BigDecimal -> {@code BigDecimal::new}</li>
     * <li>BigInteger -> {@code BigInteger::new}</li>
     * <li>Double -> {@code Double::valueOf}</li>
     * <li>Float -> {@code Float::valueOf}</li>
     * <li>Long -> {@code Long::valueOf}</li>
     * <li>Integer -> {@code Integer::valueOf}</li>
     * <li>Short -> {@code Short::valueOf}</li>
     * <li>Byte -> {@code Byte::valueOf}</li>
     * </ul>
     *
     * @param type numeric value type
     * @param parser text parser
     * @param <T> type of numeric value
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends Number & Comparable<T>> void addParser(
            Class<T> type, @NonNull Function<String, T> parser) {
        NUMERIC_TEXT_PARSER_MAP.put(type, (Function) parser);
    }

    // #################### internal utils ################################################

    /** numeric text parsers */
    private static final Map<Class<?>, Function<String, ?>> NUMERIC_TEXT_PARSER_MAP;

    static {
        NUMERIC_TEXT_PARSER_MAP = new HashMap<>();
        addParser(BigDecimal.class, BigDecimal::new);
        addParser(BigInteger.class, BigInteger::new);
        addParser(Double.class, Double::valueOf);
        addParser(Float.class, Float::valueOf);
        addParser(Long.class, Long::valueOf);
        addParser(Integer.class, Integer::valueOf);
        addParser(Short.class, Short::valueOf);
        addParser(Byte.class, Byte::valueOf);
    }

}
