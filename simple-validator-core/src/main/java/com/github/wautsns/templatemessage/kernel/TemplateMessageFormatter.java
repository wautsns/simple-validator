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
import lombok.Data;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Formatter for {@code TemplateMessage} value.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
public class TemplateMessageFormatter implements Formatter<TemplateMessage> {

    /** Ordered processors map. */
    private final Map<Integer, Collection<Processor>> orderedProcessorsMap = new ConcurrentSkipListMap<>();

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
        orderedProcessorsMap
                .computeIfAbsent(order, i -> new ConcurrentLinkedQueue<>())
                .add(processor);
        return this;
    }

    /**
     * Format the template message with {@linkplain Locale#getDefault() default locale}.
     *
     * @param templateMessage template message
     * @param locale locale
     * @return value in string format
     */
    @Override
    public String format(TemplateMessage templateMessage, Locale locale) {
        return process(templateMessage.getMessageTemplate(), templateMessage, locale);
    }

    /** Processor when formatting variable values. */
    @Data
    public abstract static class Processor {

        /** Left delimiter. */
        private final String leftDelimiter;
        /** Right delimiter. */
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

    // #################### internal utils ################################################

    /**
     * Process the wip of the message.
     *
     * @param wip wip of the message
     * @param variableValueMap variable value map
     * @param locale locale
     * @return message after processing
     */
    private String process(CharSequence wip, VariableValueMap variableValueMap, Locale locale) {
        StringBuilder tmp = new StringBuilder(wip);
        orderedProcessorsMap.forEach(
                (order, processors) -> processors.forEach(
                        processor -> process(tmp, processor, variableValueMap, locale)));
        if (tmp.length() != wip.length()) {
            return process(tmp, variableValueMap, locale);
        } else {
            for (int i = 0; i < tmp.length(); i++) {
                if (tmp.charAt(i) != wip.charAt(i)) {
                    return process(tmp, variableValueMap, locale);
                }
            }
            return tmp.toString();
        }
    }

    /**
     * Process the wip of the message with the specified processor.
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

}
