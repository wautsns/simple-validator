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
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.util.CriterionUtils;
import lombok.experimental.UtilityClass;

/**
 * Validator utility.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@UtilityClass
@SuppressWarnings({"rawtypes", "unchecked"})
public class Validator {

    // -------------------- test --------------------------------------------------------

    /**
     * Test the value.
     *
     * @param value value
     * @return {@code true} if the value passes the testing, otherwise {@code false}
     */
    public static boolean test(Object value) {
        return (validateGently(value) == null);
    }

    /**
     * Test the value with the specific class.
     *
     * @param value value
     * @param <T> type of value
     * @return {@code true} if the value passes the testing, otherwise {@code false}
     */
    public static <T> boolean test(Class<? super T> clazz, T value) {
        return (validateGently(clazz, value) == null);
    }

    // -------------------- validate ----------------------------------------------------

    /**
     * Validate value rudely.
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
     * Validate value with specific class rudely.
     *
     * @param value value
     * @param <T> type of value
     * @return original value
     * @throws ValidationException if the value fails the validation
     */
    public static <T> T validateRudely(Class<? super T> clazz, T value) throws ValidationException {
        ValidationFailure failure = validateGently(clazz, value);
        if (failure == null) {
            return value;
        } else {
            throw new ValidationException(failure);
        }
    }

    /**
     * Validate value gently.
     *
     * @param value value
     * @return validation failure, or {@code null} if the value passes the validation
     */
    public static ValidationFailure validateGently(Object value) {
        return validateGently((Class) value.getClass(), value);
    }

    /**
     * Validate value with specific class gently.
     *
     * @param value value
     * @param <T> type of value
     * @return validation failure, or {@code null} if the value passes the validation
     */
    public static <T> ValidationFailure validateGently(Class<? super T> clazz, T value) {
        return CriterionUtils.execute(CriterionUtils.forClass(clazz), value);
    }

}
