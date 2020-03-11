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
package com.github.wautsns.simplevalidator.model.criterion.factory.typelike;

import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.templatemessage.formatter.Formatter;
import com.github.wautsns.templatemessage.variable.Variable;

import java.lang.reflect.Type;

/**
 * Type like utility.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class TypeLikeUtility<T> {

    /** variable for the type like value */
    private final Variable<? super T> valueVariable;

    public TypeLikeUtility() {
        this.valueVariable = ValidationFailure.Variables.VALUE;
    }

    public TypeLikeUtility(Formatter<? super T> valueFormatter) {
        this.valueVariable = new Variable<>(ValidationFailure.Variables.VALUE.getName(), valueFormatter);
    }

    /**
     * Return {@code true} if the utility applies to the type, otherwise {@code false}.
     *
     * @param type type
     * @return {@code true} if the utility applies to the type, otherwise {@code false}
     */
    public abstract boolean appliesTo(Type type);

    /**
     * Return validation failure.
     *
     * @param value value
     * @return validation failure
     */
    public final ValidationFailure fail(T value) {
        return new ValidationFailure(valueVariable, value);
    }

    /**
     * Initialize type like variable.
     *
     * @param name name of the variable
     * @return type like variable
     */
    public final Variable<T> initTypeLikeVariable(String name) {
        return new Variable<>(name, valueVariable.getFormatter());
    }

}
