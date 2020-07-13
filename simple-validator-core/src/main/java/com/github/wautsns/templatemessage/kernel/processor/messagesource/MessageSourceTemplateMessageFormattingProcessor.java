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
package com.github.wautsns.templatemessage.kernel.processor.messagesource;

import com.github.wautsns.templatemessage.kernel.TemplateMessageFormatter;
import com.github.wautsns.templatemessage.variable.VariableValueMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * Template message formatting processor for {@link MessageSource message source}.
 *
 * @author wautsns
 * @since Mar 24, 2020
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class MessageSourceTemplateMessageFormattingProcessor<T extends MessageSource>
        extends TemplateMessageFormatter.Processor {

    /** Message source. */
    private final T messageSource;

    /**
     * Construct a messageSourceTemplateMessageFormattingProcessor.
     *
     * @param leftDelimiter left delimiter
     * @param rightDelimiter right delimiter
     * @param messageSource message source
     */
    public MessageSourceTemplateMessageFormattingProcessor(
            String leftDelimiter, String rightDelimiter, T messageSource) {
        super(leftDelimiter, rightDelimiter);
        this.messageSource = messageSource;
    }

    @Override
    public String process(String text, VariableValueMap variableValueMap, Locale locale) {
        return messageSource.getMessage(text, null, null, locale);
    }

}
