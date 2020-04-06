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
package com.github.wautsns.templatemessage.kernel;

import com.github.wautsns.templatemessage.variable.Variable;
import com.github.wautsns.templatemessage.variable.VariableValueMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Template message.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class TemplateMessage extends VariableValueMap {

    private static final long serialVersionUID = -471813798916205381L;

    /** message template */
    private String messageTemplate;

    @Override
    public TemplateMessage put(VariableValueMap variableValueMap) {
        return (TemplateMessage) super.put(variableValueMap);
    }

    @Override
    public <T> TemplateMessage put(Variable<T> variable, T value) {
        return (TemplateMessage) super.put(variable, value);
    }

    @Override
    public TemplateMessage remove(Variable<?> variable) {
        return (TemplateMessage) super.remove(variable);
    }

    @Override
    public String toString() {
        return messageTemplate + "<==" + super.toString();
    }

}
