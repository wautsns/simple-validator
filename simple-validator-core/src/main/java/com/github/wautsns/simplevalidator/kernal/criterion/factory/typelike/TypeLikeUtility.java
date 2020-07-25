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
package com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike;

import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.templatemessage.formatter.Formatter;
import com.github.wautsns.templatemessage.variable.Variable;

import java.lang.reflect.Type;

/**
 * Type-like utility.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class TypeLikeUtility<T> {

    /** Variable for the type-like value. */
    private final Variable<? super T> variable;

    /**
     * Construct a type-like utility.
     *
     * @param formatter value formatter
     */
    public TypeLikeUtility(Formatter<? super T> formatter) {
        if (formatter == null) {
            this.variable = ValidationFailure.Variables.VALUE;
        } else {
            this.variable = new Variable<>(ValidationFailure.Variables.VALUE.getName(), formatter);
        }
    }

    /**
     * Return whether the utility applies to the specified type.
     *
     * @param type type
     * @return {@code true} if the utility applies to the specified type, otherwise {@code false}
     */
    public abstract boolean applyTo(Type type);

    /**
     * Return validation failure with the specified value.
     *
     * @param value value
     * @return validation failure with the specified value
     */
    public final ValidationFailure fail(T value) {
        return new ValidationFailure(variable, value);
    }

    /**
     * Initialize type-like variable with the specified name.
     *
     * @param name name of the variable
     * @return type-like variable with the specified name
     */
    public final Variable<T> initTypeLikeVariable(String name) {
        return new Variable<>(name, variable.getFormatter());
    }

}
