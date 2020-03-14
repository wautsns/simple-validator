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
import com.github.wautsns.templatemessage.kernel.processor.PropertiesFormattingProcessor;
import com.github.wautsns.templatemessage.kernel.processor.SpelFormattingProcessor;
import com.github.wautsns.templatemessage.kernel.processor.VariableFormattingProcessor;

/**
 * Validation failure formatter.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
public class ValidationFailureFormatter extends TemplateMessageFormatter {

    private static final long serialVersionUID = -6637627708711439692L;

    public static final String LEFT_DELIMITER_VARIABLE = "{{";
    public static final String RIGHT_DELIMITER_VARIABLE = "}}";
    public static final String LEFT_DELIMITER_PROPERTIES = "[`";
    public static final String RIGHT_DELIMITER_PROPERTIES = "`]";
    public static final String LEFT_DELIMITER_SPEL = "#{";
    public static final String RIGHT_DELIMITER_SPEL = "}#";

    private final PropertiesFormattingProcessor propertiesFormattingProcessor;

    public ValidationFailureFormatter() {
        // dynamic variable format
        addProcessor(0, new VariableFormattingProcessor(LEFT_DELIMITER_VARIABLE, RIGHT_DELIMITER_VARIABLE));
        // properties variable format
        propertiesFormattingProcessor =
                new PropertiesFormattingProcessor(LEFT_DELIMITER_PROPERTIES, RIGHT_DELIMITER_PROPERTIES)
                        .load("simple-validator/messages/", "messages");
        addProcessor(100, propertiesFormattingProcessor);
        // spel variable format
        addProcessor(200, new SpelFormattingProcessor(LEFT_DELIMITER_SPEL, RIGHT_DELIMITER_SPEL));
    }

    /**
     * Load properties.
     *
     * <p>eg. load("/i18n", "messages") -> /i18n/messages_zh.properties; /i18n/messages_en.properties
     *
     * @param folderPath folder path
     * @param baseName base name of properties
     * @return self reference
     */
    public ValidationFailureFormatter loadProperties(String folderPath, String baseName) {
        propertiesFormattingProcessor.load(folderPath, baseName);
        return this;
    }

    @Override
    public ValidationFailureFormatter addProcessor(int order, Processor processor) {
        return (ValidationFailureFormatter) super.addProcessor(order, processor);
    }

}
