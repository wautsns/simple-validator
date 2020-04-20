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

import com.github.wautsns.templatemessage.kernel.TemplateMessageFormatter;
import com.github.wautsns.templatemessage.kernel.processor.SpelTemplateMessageFormattingProcessor;
import com.github.wautsns.templatemessage.kernel.processor.VariableTemplateMessageFormattingProcessor;
import com.github.wautsns.templatemessage.kernel.processor.messagesource.ReloadableResourceTemplateMessageFormattingProcessor;
import lombok.Getter;

/**
 * Validation failure formatter.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Getter
public class ValidationFailureFormatter extends TemplateMessageFormatter {

    /** the left delimiter for variable processor */
    public static final String VARIABLE_LD = "{{";
    /** the right delimiter for variable processor */
    public static final String VARIABLE_RD = "}}";
    /** the left delimiter for resource processor */
    public static final String RELOADED_RESOURCE_LD = "[`";
    /** the right delimiter for resource processor */
    public static final String RELOADED_RESOURCE_RD = "`]";
    /** the left delimiter for spel processor */
    public static final String SPEL_LD = "#{";
    /** the right delimiter for spel processor */
    public static final String SPEL_RD = "}#";

    /** built-in messages base name */
    private static final String BUILT_IN_MESSAGES_BASE_NAME = "simple-validator/messages/messages";

    /** reloadable resource template message formatting processor */
    private final ReloadableResourceTemplateMessageFormattingProcessor reloadableResourceTemplateMessageFormattingProcessor;

    /**
     * Construct a validation failure formatter.
     *
     * <ul>
     * default processors are as followers:
     * <li>100: {@link VariableTemplateMessageFormattingProcessor}</li>
     * <li>200: {@link ReloadableResourceTemplateMessageFormattingProcessor}</li>
     * <li>300: {@link SpelTemplateMessageFormattingProcessor}</li>
     * </ul>
     */
    public ValidationFailureFormatter() {
        // variable formatting processor
        addProcessor(100, new VariableTemplateMessageFormattingProcessor(VARIABLE_LD, VARIABLE_RD));
        // reloadable resource formatting processor
        reloadableResourceTemplateMessageFormattingProcessor = new ReloadableResourceTemplateMessageFormattingProcessor(
                RELOADED_RESOURCE_LD, RELOADED_RESOURCE_RD);
        reloadableResourceTemplateMessageFormattingProcessor.loadResources(BUILT_IN_MESSAGES_BASE_NAME);
        addProcessor(200, reloadableResourceTemplateMessageFormattingProcessor);
        // spel formatting processor
        addProcessor(300, new SpelTemplateMessageFormattingProcessor(SPEL_LD, SPEL_RD));
    }

    @Override
    public ValidationFailureFormatter addProcessor(int order, Processor processor) {
        return (ValidationFailureFormatter) super.addProcessor(order, processor);
    }

}
