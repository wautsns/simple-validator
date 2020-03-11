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
package com.github.wautsns.simplevalidator.model.criterion.kernel;

import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.UnaryOperator;

/**
 * Criterion.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public interface Criterion {

    /**
     * Enhance failure.
     *
     * @param operator failure operator
     * @return new criterion after enhancing
     */
    Criterion enhanceResultProcessing(UnaryOperator<ValidationFailure> operator);

    /**
     * Wrap the criterion(applies to field) into the criterion applies to the declared type.
     *
     * @param field field
     * @return criterion applies to the declared type
     */
    TCriterion<?> wrapFieldIntoType(Field field);

    /**
     * Wrap the criterion(applies to getter) into the criterion applies to declared type.
     *
     * @param getter getter
     * @return criterion applies to the declared type
     */
    TCriterion<?> wrapGetterIntoType(Method getter);

    /**
     * Wrap the criterion(applies to array component) into the criterion applies to array.
     *
     * @return criterion applies to array
     */
    TCriterion<?> wrapComponentIntoArray();

    /**
     * Wrap the criterion(applies to iterable element) into the criterion applies to iterable.
     *
     * @return criterion applies to iterable
     */
    TCriterion<?> wrapElementIntoIterable();

    /**
     * Wrap the criterion(applies to map key) into the criterion applies to map.
     *
     * @return criterion applies to map
     */
    TCriterion<?> wrapKeyIntoMap();

    /**
     * Wrap the criterion(applies to map value) into the criterion applies to map.
     *
     * @return criterion applies to map
     */
    TCriterion<?> wrapValueIntoMap();

}
