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
@SuppressWarnings({"rawtypes", "unchecked"})
public class Validator {

    /**
     * Test the value.
     *
     * @param value value
     * @return {@code true} if the value passes the test, otherwise {@code false}
     */
    public static boolean test(Object value) {
        return (validateGently(value) == null);
    }

    /**
     * Test the value with the specified type.
     *
     * @param type benchmark type of test
     * @param value value
     * @param <T> type of value
     * @return {@code true} if the value passes the test, otherwise {@code false}
     */
    public static <T> boolean test(Class<? super T> type, T value) {
        return (validateGently(type, value) == null);
    }

    /**
     * Validate the value rudely.(i.e. throw {@code ValidationException} directly)
     *
     * @param value value
     * @param <T> type of value
     * @return original value
     * @throws ValidationException if the value fails the validation
     */
    public static <T> T validateRudely(T value) throws ValidationException {
        return validateRudely((Class<T>) value.getClass(), value);
    }

    /**
     * Validate the value rudely with the specified type.(i.e. throw {@code ValidationException} directly)
     *
     * @param type benchmark type of validation
     * @param value value
     * @param <T> type of value
     * @return original value
     * @throws ValidationException if the value fails the validation
     */
    public static <T> T validateRudely(Class<? super T> type, T value) throws ValidationException {
        ValidationFailure failure = validateGently(type, value);
        if (failure == null) { return value; }
        throw new ValidationException(failure);
    }

    /**
     * Validate value gently.(i.e. return {@code ValidationException} instead of throw)
     *
     * @param value value
     * @return validation failure, or {@code null} if the value passes the validation
     */
    public static ValidationFailure validateGently(Object value) {
        return validateGently((Class) value.getClass(), value);
    }

    /**
     * Validate value gently with specified type.(i.e. return {@code ValidationException} instead of throw)
     *
     * @param type benchmark type of validation
     * @param value value
     * @param <T> type of value
     * @return validation failure, or {@code null} if the value passes the validation
     */
    public static <T> ValidationFailure validateGently(Class<? super T> type, T value) {
        return CriterionUtils.execute(CriterionUtils.forType(type), value);
    }

}
