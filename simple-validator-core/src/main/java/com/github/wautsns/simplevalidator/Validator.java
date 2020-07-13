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
package com.github.wautsns.simplevalidator;

import com.github.wautsns.simplevalidator.exception.ValidationException;
import com.github.wautsns.simplevalidator.model.criterion.util.CriterionUtils;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import lombok.experimental.UtilityClass;

/**
 * Validator.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@UtilityClass
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Validator {

    /**
     * Test value.
     *
     * @param value value
     * @return {@code true} if the value passes the test, otherwise {@code false}
     */
    public static boolean test(Object value) {
        return (validatePolitely(value) == null);
    }

    /**
     * Test value with the specified type.
     *
     * @param type benchmark type of test
     * @param value value
     * @param <T> type of value
     * @return {@code true} if the value passes the test, otherwise {@code false}
     */
    public static <T> boolean test(Class<? super T> type, T value) {
        return (validatePolitely(type, value) == null);
    }

    /**
     * Validate value politely.
     *
     * @param value value
     * @return validation failure, or {@code null} if the value passes the validation
     */
    public static ValidationFailure validatePolitely(Object value) {
        return validatePolitely((Class) value.getClass(), value);
    }

    /**
     * Validate value with specified type politely.
     *
     * @param type benchmark type of validation
     * @param value value
     * @param <T> type of value
     * @return validation failure, or {@code null} if the value passes the validation
     */
    public static <T> ValidationFailure validatePolitely(Class<? super T> type, T value) {
        return CriterionUtils.execute(CriterionUtils.forType(type), value);
    }

    /**
     * Validate value rudely.
     *
     * @param value value
     * @param <T> type of value
     * @return validation failure, or {@code null} if the value passes the validation
     */
    public static <T> T validateRudely(T value) {
        ValidationFailure failure = validatePolitely((Class) value.getClass(), value);
        if (failure == null) { return value; }
        throw new ValidationException(failure);
    }

    /**
     * Validate value with specified type rudely.
     *
     * @param type benchmark type of validation
     * @param value value
     * @param <T> type of value
     * @return validation failure, or {@code null} if the value passes the validation
     */
    public static <T> T validateRudely(Class<? super T> type, T value) {
        ValidationFailure failure = validatePolitely(type, value);
        if (failure == null) { return value; }
        throw new ValidationException(failure);
    }

}
