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
import com.github.wautsns.templatemessage.variable.VariableValueMap;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.DataBindingPropertyAccessor;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.util.Locale;

/**
 * Template message formatting processor for Spel(Spring expression language).
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
public class SpelFormattingProcessor extends TemplateMessageFormatter.Processor {

    /** serialVersionUID */
    private static final long serialVersionUID = 4480932903559439109L;

    /** spel expression parser */
    private static final ExpressionParser parser = new SpelExpressionParser();

    public SpelFormattingProcessor(String leftDelimiter, String rightDelimiter) {
        super(leftDelimiter, rightDelimiter);
    }

    // #################### process #####################################################

    @Override
    public String process(String text, VariableValueMap variableValueMap, Locale locale) {
        try {
            Expression expr = parser.parseExpression(text);
            SimpleEvaluationContext ctx = SimpleEvaluationContext
                    .forPropertyAccessors(PROPERTY_ACCESSORS)
                    .withRootObject(variableValueMap)
                    .build();
            return expr.getValue(ctx, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    // #################### internal utils ##############################################

    /** property accessors */
    private static final PropertyAccessor[] PROPERTY_ACCESSORS = new PropertyAccessor[]{
            new VariableValueMapAccessor(), DataBindingPropertyAccessor.forReadOnlyAccess()
    };

    /** Variable value map accessor. */
    private static class VariableValueMapAccessor implements PropertyAccessor {

        /** specific target classes */
        private static final Class<?>[] SPECIFIC_TARGET_CLASSES = new Class[]{VariableValueMap.class};

        @Override
        public Class<?>[] getSpecificTargetClasses() {
            return SPECIFIC_TARGET_CLASSES;
        }

        @Override
        public boolean canRead(EvaluationContext context, Object target, String name) {
            return ((VariableValueMap) target).getVariable(name) != null;
        }

        @Override
        public TypedValue read(EvaluationContext context, Object target, String name) {
            VariableValueMap variableValueMap = (VariableValueMap) target;
            return new TypedValue(variableValueMap.getValue(variableValueMap.getVariable(name)));
        }

        @Override
        public boolean canWrite(EvaluationContext context, Object target, String name) {
            return false;
        }

        @Override
        public void write(EvaluationContext context, Object target, String name, Object newValue) {
            throw new UnsupportedOperationException();
        }

    }

}
