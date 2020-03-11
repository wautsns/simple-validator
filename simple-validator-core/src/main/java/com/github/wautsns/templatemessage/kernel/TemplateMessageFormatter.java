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

import com.github.wautsns.templatemessage.formatter.Formatter;
import com.github.wautsns.templatemessage.variable.VariableValueMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Formatter for {@code TemplateMessageFormatter} value.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
public class TemplateMessageFormatter implements Formatter<TemplateMessage> {

    private static final long serialVersionUID = -316723311454568554L;

    private final TreeMap<Integer, List<Processor>> processors = new TreeMap<>();

    @Override
    public boolean appliesTo(Class<?> clazz) {
        return TemplateMessage.class.isAssignableFrom(clazz);
    }

    @Override
    public String format(TemplateMessage value, Locale locale) {
        return null;
    }

    /**
     * Add a processor.
     *
     * @param order order
     * @param processor processor
     * @return self reference
     */
    public TemplateMessageFormatter addProcessor(int order, Processor processor) {
        processors.computeIfAbsent(order, i -> new LinkedList<>()).add(processor);
        return this;
    }

    /** Processor variable when formatting. */
    @Getter
    @RequiredArgsConstructor
    public abstract static class Processor implements Serializable {

        private static final long serialVersionUID = -5793432939078583151L;

        /** left delimiter */
        private final String leftDelimiter;
        /** right delimiter */
        private final String rightDelimiter;

        /**
         * Process variable.
         *
         * @param name a name of the variable
         * @param variableValueMap variable value map
         * @param locale locale
         * @return string format of the value, or {@code null} if the processor cannot process the value
         */
        public abstract String process(String name, VariableValueMap variableValueMap, Locale locale);

    }

}
