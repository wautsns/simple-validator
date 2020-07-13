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
package com.github.wautsns.templatemessage.kernel.processor;

import com.github.wautsns.templatemessage.kernel.TemplateMessageFormatter;
import com.github.wautsns.templatemessage.variable.Variable;
import com.github.wautsns.templatemessage.variable.VariableValueMap;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Locale;

/**
 * Template message formatting processor for {@linkplain Variable variable}.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class VariableTemplateMessageFormattingProcessor extends TemplateMessageFormatter.Processor {

    /**
     * Construct a variableTemplateMessageFormattingProcessor.
     *
     * @param leftDelimiter left delimiter
     * @param rightDelimiter right delimiter
     */
    public VariableTemplateMessageFormattingProcessor(String leftDelimiter, String rightDelimiter) {
        super(leftDelimiter, rightDelimiter);
    }

    @Override
    public String process(String text, VariableValueMap variableValueMap, Locale locale) {
        Variable<Object> variable = variableValueMap.getVariable(text);
        if (variable == null) { return null; }
        Object value = variableValueMap.getValue(variable);
        return variable.getFormatter().format(value, locale);
    }

}
