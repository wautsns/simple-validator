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

import com.github.wautsns.templatemessage.kernel.TemplateMessage;
import com.github.wautsns.templatemessage.kernel.TemplateMessageFormatter;
import com.github.wautsns.templatemessage.kernel.processor.SpelTemplateMessageFormattingProcessor;
import com.github.wautsns.templatemessage.kernel.processor.VariableTemplateMessageFormattingProcessor;
import com.github.wautsns.templatemessage.kernel.processor.messagesource.ReloadableResourceTemplateMessageFormattingProcessor;
import com.github.wautsns.templatemessage.variable.Variable;
import lombok.Getter;

import java.util.Locale;

/**
 * Validation failure formatter.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Getter
public class ValidationFailureFormatter extends TemplateMessageFormatter {

    /** Left delimiter for the variable processor. */
    public static final String VARIABLE_LD = "{{";
    /** Right delimiter for the variable processor. */
    public static final String VARIABLE_RD = "}}";
    /** Left delimiter for the resource processor. */
    public static final String RELOADED_RESOURCE_LD = "[`";
    /** Right delimiter for the resource processor. */
    public static final String RELOADED_RESOURCE_RD = "`]";
    /** Left delimiter for the spel processor. */
    public static final String SPEL_LD = "#{";
    /** Right delimiter for the spel processor. */
    public static final String SPEL_RD = "}#";

    /** Built-in messages base name. */
    private static final String BUILT_IN_MESSAGES_BASE_NAME = "simple-validator/messages/messages";
    /** Variable: value. */
    private static final String VARIABLE_VALUE_PLACEHOLDER =
            VARIABLE_LD + ValidationFailure.Variables.VALUE.getName() + VARIABLE_RD;

    /** Reloadable resource template message formatting processor. */
    private final ReloadableResourceTemplateMessageFormattingProcessor reloadableResourceTemplateMessageFormattingProcessor;

    /**
     * Construct a validation failure formatter.
     *
     * <pre>
     * default processors are as followers:
     *   100: {@link VariableTemplateMessageFormattingProcessor}
     *   200: {@link ReloadableResourceTemplateMessageFormattingProcessor}
     *   300: {@link SpelTemplateMessageFormattingProcessor}
     * </pre>
     */
    public ValidationFailureFormatter() {
        // variable formatting processor
        addProcessor(100, new VariableTemplateMessageFormattingProcessor(VARIABLE_LD, VARIABLE_RD));
        // reloadable resource formatting processor
        reloadableResourceTemplateMessageFormattingProcessor = new ReloadableResourceTemplateMessageFormattingProcessor(
                RELOADED_RESOURCE_LD, RELOADED_RESOURCE_RD);
        reloadableResourceTemplateMessageFormattingProcessor.loadMessageResources(BUILT_IN_MESSAGES_BASE_NAME);
        addProcessor(200, reloadableResourceTemplateMessageFormattingProcessor);
        // spel formatting processor
        addProcessor(300, new SpelTemplateMessageFormattingProcessor(SPEL_LD, SPEL_RD));
    }

    @Override
    public ValidationFailureFormatter addProcessor(int order, Processor processor) {
        return (ValidationFailureFormatter) super.addProcessor(order, processor);
    }

    /**
     * Load message resources.
     *
     * @param messageResources message resources.
     * @return self reference
     */
    public ValidationFailureFormatter loadMessageResources(String[] messageResources) {
        reloadableResourceTemplateMessageFormattingProcessor.loadMessageResources(messageResources);
        return this;
    }

    @Override
    public String format(TemplateMessage templateMessage, Locale locale) {
        // Prevent spel expression injection.
        Variable<Object> variable = templateMessage.getVariable(ValidationFailure.Variables.VALUE.getName());
        Object value = templateMessage.getValue(variable);
        templateMessage.remove(variable);
        String wip = super.format(templateMessage, locale);
        String valueInStringFormat = variable.getFormatter().format(value, locale);
        return wip.replace(VARIABLE_VALUE_PLACEHOLDER, valueInStringFormat);
    }

}
