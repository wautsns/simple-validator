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
package com.github.wautsns.simplevalidator.model.failure;

import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.templatemessage.kernel.TemplateMessage;
import com.github.wautsns.templatemessage.variable.Variable;
import com.github.wautsns.templatemessage.variable.VariableValueMap;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;

/**
 * Validation failure.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class ValidationFailure extends TemplateMessage {

    /**
     * Construct a validation failure with {@link Variables#VALUE default value variable} and specified value.
     *
     * @param value value
     */
    public ValidationFailure(Object value) {
        put(Variables.VALUE, value);
    }

    /**
     * Construct a validation failure with specified valueVariable and value.
     *
     * @param valueVariable value variable
     * @param value value
     * @param <T> type of value
     */
    public <T> ValidationFailure(Variable<T> valueVariable, T value) {
        put(valueVariable, value);
    }

    @Override
    public ValidationFailure setMessageTemplate(String messageTemplate) {
        return (ValidationFailure) super.setMessageTemplate(messageTemplate);
    }

    /**
     * Add an indicator.
     *
     * @param indicator indicator
     * @return self reference
     * @see Variables#INDICATORS
     */
    public ValidationFailure addIndicator(Object indicator) {
        List<Object> indicators = getValue(Variables.INDICATORS);
        if (indicators == null) {
            indicators = new LinkedList<>();
            put(Variables.INDICATORS, indicators);
        }
        indicators.add(indicator);
        return this;
    }

    @Override
    public ValidationFailure put(VariableValueMap variableValueMap) {
        return (ValidationFailure) super.put(variableValueMap);
    }

    @Override
    public <T> ValidationFailure put(Variable<T> variable, T value) {
        return (ValidationFailure) super.put(variable, value);
    }

    @Override
    public ValidationFailure remove(Variable<?> variable) {
        return (ValidationFailure) super.remove(variable);
    }

    /** Variables. */
    @UtilityClass
    public static class Variables {

        /** Variable: value. */
        public static final Variable<Object> VALUE = new Variable<>("value");
        /** Variable: location. */
        public static final Variable<ConstrainedNode.Location> LOCATION = new Variable<>("location");
        /**
         * Variable: indicators.
         *
         * <ul>
         * <li>array[1] =&gt; indicator is 1</li>
         * <li>map[key] =&gt; indicator is key</li>
         * <li>customize...</li>
         * </ul>
         */
        public static final Variable<List<Object>> INDICATORS = new Variable<>("indicators");

    }

}
