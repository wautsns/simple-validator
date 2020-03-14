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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

/**
 * Formatter for {@code TemplateMessageFormatter} value.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@EqualsAndHashCode
public class TemplateMessageFormatter implements Formatter<TemplateMessage> {

    private static final long serialVersionUID = -316723311454568554L;

    /** processors */
    private final TreeMap<Integer, Set<Processor>> processors = new TreeMap<>();

    @Override
    public String format(TemplateMessage templateMessage, Locale locale) {
        return process(templateMessage.getMessageTemplate(), templateMessage, locale);
    }

    /**
     * Add a processor.
     *
     * <p>Processors of the same order will coexist.
     *
     * @param order the order of the processor
     * @param processor processor
     * @return self reference
     */
    public TemplateMessageFormatter addProcessor(int order, Processor processor) {
        processors.computeIfAbsent(order, i -> new HashSet<>(2, 1F)).add(processor);
        return this;
    }

    /**
     * Process the wip of the message.
     *
     * @param wip wip of the message
     * @param variableValueMap variable value map
     * @param locale locale
     * @return message after processing
     */
    private String process(CharSequence wip, VariableValueMap variableValueMap, Locale locale) {
        StringBuilder temp = new StringBuilder(wip);
        processors.forEach((order, ps) -> ps.forEach(p -> process(temp, p, variableValueMap, locale)));
        if (temp.length() != wip.length()) {
            return process(temp, variableValueMap, locale);
        } else {
            for (int i = 0; i < temp.length(); i++) {
                if (temp.charAt(i) != wip.charAt(i)) {
                    return process(temp, variableValueMap, locale);
                }
            }
            return temp.toString();
        }
    }

    /**
     * Process the wip of the message with the specific processor.
     *
     * @param wip wip of the message
     * @param processor processor
     * @param variableValueMap variable value map
     * @param locale locale
     */
    private void process(StringBuilder wip, Processor processor, VariableValueMap variableValueMap, Locale locale) {
        int si;
        int ei = 0;
        String ld = processor.getLeftDelimiter();
        String rd = processor.getRightDelimiter();
        while (true) {
            si = wip.indexOf(ld, ei);
            if (si == -1) { return; }
            ei = wip.indexOf(rd, si + ld.length());
            if (ei == -1) { return; }
            int tmpEi = ei;
            while (tmpEi != -1) {
                String name = wip.substring(si + ld.length(), tmpEi).trim();
                String value = processor.process(name, variableValueMap, locale);
                if (value == null) {
                    ei = si + 1;
                    tmpEi = wip.indexOf(rd, tmpEi + 1);
                } else {
                    wip.replace(si, tmpEi + rd.length(), value);
                    ei = si + value.length();
                    break;
                }
            }
        }
    }

    /** Processor when formatting variable values. */
    @Getter
    @EqualsAndHashCode
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
         * @param text text between delimiter
         * @param variableValueMap variable value map
         * @param locale locale
         * @return string associated with the text, or {@code null} if the processor cannot process the text
         */
        public abstract String process(String text, VariableValueMap variableValueMap, Locale locale);

    }

}