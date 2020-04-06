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
import com.github.wautsns.templatemessage.kernel.processor.SpelFormattingProcessor;
import com.github.wautsns.templatemessage.kernel.processor.VariableFormattingProcessor;
import com.github.wautsns.templatemessage.kernel.processor.messagesource.ReloadableResourceFormattingProcessor;

/**
 * Validation failure formatter.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
public class ValidationFailureFormatter extends TemplateMessageFormatter {

    private static final long serialVersionUID = -6637627708711439692L;

    /** the left delimiter for variable processor */
    public static final String LEFT_DELIMITER_VARIABLE = "{{";
    /** the right delimiter for variable processor */
    public static final String RIGHT_DELIMITER_VARIABLE = "}}";
    /** the left delimiter for resource processor */
    public static final String LEFT_DELIMITER_RESOURCE = "[`";
    /** the right delimiter for resource processor */
    public static final String RIGHT_DELIMITER_RESOURCE = "`]";
    /** the left delimiter for spel processor */
    public static final String LEFT_DELIMITER_SPEL = "#{";
    /** the right delimiter for spel processor */
    public static final String RIGHT_DELIMITER_SPEL = "}#";

    /** built-in messages base name */
    private static final String BUILT_IN_MESSAGES_BASE_NAME = "simple-validator/messages/messages";

    /** reloadable resource formatting processor */
    private final ReloadableResourceFormattingProcessor reloadableResourceFormattingProcessor;

    /**
     * Construct a validation failure formatter.
     *
     * <ul>
     * default processors are as followers:
     * <li>{@link VariableFormattingProcessor}</li>
     * <li>{@link ReloadableResourceFormattingProcessor}</li>
     * <li>{@link SpelFormattingProcessor}</li>
     * </ul>
     */
    public ValidationFailureFormatter() {
        // variable formatting processor
        addProcessor(0, new VariableFormattingProcessor(LEFT_DELIMITER_VARIABLE, RIGHT_DELIMITER_VARIABLE));
        // reloadable resource formatting processor
        reloadableResourceFormattingProcessor = new ReloadableResourceFormattingProcessor(
                LEFT_DELIMITER_RESOURCE, RIGHT_DELIMITER_RESOURCE);
        loadResources(BUILT_IN_MESSAGES_BASE_NAME);
        addProcessor(100, reloadableResourceFormattingProcessor);
        // spel formatting processor
        addProcessor(200, new SpelFormattingProcessor(LEFT_DELIMITER_SPEL, RIGHT_DELIMITER_SPEL));
    }

    /**
     * Load resources.
     *
     * @param baseNames base names
     * @return self reference
     * @see ReloadableResourceFormattingProcessor#loadResources(String...)
     */
    public ValidationFailureFormatter loadResources(String... baseNames) {
        reloadableResourceFormattingProcessor.loadResources(baseNames);
        return this;
    }

    @Override
    public ValidationFailureFormatter addProcessor(int order, Processor processor) {
        return (ValidationFailureFormatter) super.addProcessor(order, processor);
    }

}
