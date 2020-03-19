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
package com.github.wautsns.simplevalidator.model.criterion.processor;

import com.github.wautsns.simplevalidator.constraint.AAttribute;
import com.github.wautsns.simplevalidator.constraint.ACombine;
import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.exception.analysis.IllegalConstrainedNodeException;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.ConstraintUtils;
import com.github.wautsns.templatemessage.variable.VariableValueMap;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.DataBindingPropertyAccessor;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Constraint criterion processor.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConstraintCriterionProcessor<A extends Annotation> {

    /** constraint */
    private final A constraint;
    /** order of the constraint */
    private final int order;
    /** criterion factories of the constraint */
    private final List<CriterionFactory<A, ?, ?>> criterionFactories;
    /** preprocessor */
    private final List<ConstraintCriterionProcessor<?>> preprocessors;
    /** post processors */
    private final List<ConstraintCriterionProcessor<?>> postProcessors;

    public ConstraintCriterionProcessor(A constraint) {
        this.constraint = constraint;
        Class<A> constraintClass = (Class<A>) constraint.annotationType();
        AConstraint aconstraint = ConstraintUtils.requireAConstraint(constraintClass);
        this.order = aconstraint.order();
        this.criterionFactories = ConstraintUtils.getCriterionFactories(constraintClass);
        List<ConstraintCriterionProcessor<?>> processors = new LinkedList<>();
        processors.addAll(initProcessorsByACombines(constraint, aconstraint));
        processors.addAll(initProcessorsByDirectConstraints(constraintClass));
        this.preprocessors = new LinkedList<>();
        this.postProcessors = new LinkedList<>();
        for (ConstraintCriterionProcessor<?> processor : processors) {
            if (processor.order <= this.order) {
                preprocessors.add(processor);
            } else {
                postProcessors.add(processor);
            }
        }
    }

    private ConstraintCriterionProcessor(Annotation constraint, ACombine acombine) {
        A proxy = proxyACombine(constraint, acombine);
        this.constraint = proxy;
        Class<A> proxyClass = (Class<A>) acombine.constraint();
        AConstraint aconstraint = ConstraintUtils.requireAConstraint(proxyClass);
        this.order = acombine.order();
        this.criterionFactories = ConstraintUtils.getCriterionFactories(proxyClass);
        List<ConstraintCriterionProcessor<?>> processors = new LinkedList<>();
        processors.addAll(initProcessorsByACombines(proxy, aconstraint));
        processors.addAll(initProcessorsByDirectConstraints(proxyClass));
        this.preprocessors = new LinkedList<>();
        this.postProcessors = new LinkedList<>();
        for (ConstraintCriterionProcessor<?> processor : processors) {
            if (processor.order <= aconstraint.order()) {
                preprocessors.add(processor);
            } else {
                postProcessors.add(processor);
            }
        }
    }

    // ------------------------- process -------------------------------------------

    public void process(ConstrainedNode node, Criteria wip) {
        String message = ConstraintUtils.getMessage(constraint);
        if (message == null) {
            processWithoutVariables(node, wip);
        } else {
            Criteria tmp = Criteria.newInstance(node.getType());
            processWithoutVariables(node, tmp);
            Criterion criterion = tmp.simplify();
            if (criterion == null) { return; }
            VariableValueMap variableValueMap = ConstraintUtils.getVariableValueMap(constraint);
            variableValueMap.put(ValidationFailure.Variables.TARGET, node.getLocation());
            criterion = criterion.enhanceFailure(failure -> failure.setMessageTemplate(message).put(variableValueMap));
            wip.add(criterion);
        }
    }

    private void processWithoutVariables(ConstrainedNode node, Criteria wip) {
        preprocessors.forEach(processor -> processor.processWithoutVariables(node, wip));
        processCriterionFactories(node, wip);
        postProcessors.forEach(processor -> processor.processWithoutVariables(node, wip));
    }

    private void processCriterionFactories(ConstrainedNode node, Criteria wip) {
        if (criterionFactories.isEmpty()) { return; }
        for (CriterionFactory criterionFactory : criterionFactories) {
            if (criterionFactory.appliesTo(node, constraint)) {
                criterionFactory.process(node, constraint, wip);
                return;
            }
        }
        throw new IllegalConstrainedNodeException(node, constraint);
    }

    // ------------------------- initialize utils -----------------------------------

    private static List<ConstraintCriterionProcessor<?>> initProcessorsByDirectConstraints(
            Class<? extends Annotation> constraintClass) {
        List<ConstraintCriterionProcessor<?>> processors = new LinkedList<>();
        for (Annotation annotation : constraintClass.getDeclaredAnnotations()) {
            if (ConstraintUtils.isConstraint(annotation)) {
                processors.add(new ConstraintCriterionProcessor<>(annotation));
            }
        }
        return processors;
    }

    private static List<ConstraintCriterionProcessor<?>> initProcessorsByACombines(
            Annotation constraint, AConstraint aconstraint) {
        List<ConstraintCriterionProcessor<?>> processors = new LinkedList<>();
        for (ACombine acombine : aconstraint.combines()) {
            processors.add(new ConstraintCriterionProcessor<>(constraint, acombine));
        }
        return processors;
    }

    private static <A extends Annotation> A proxyACombine(Annotation constraint, ACombine acombine) {
        // TODO reduce complexity
        Class<A> type = (Class<A>) acombine.constraint();
        LinkedHashMap<String, Object> memberValues = new LinkedHashMap<>();
        Map<String, AAttribute> aAttributes = Arrays.stream(acombine.attributes())
                .collect(Collectors.toMap(AAttribute::name, Function.identity()));
        ConstraintUtils.getAttributes(type).forEach(attribute -> {
            String name = attribute.getName();
            Object value;
            AAttribute aattribute = aAttributes.get(name);
            if (aattribute != null) {
                String spel = aattribute.spel();
                if (!AAttribute.LOOK_VALUE.equals(spel)) {
                    Expression expr = SPEL_PARSER.parseExpression(spel);
                    SimpleEvaluationContext ctx = SimpleEvaluationContext
                            .forPropertyAccessors(PROPERTY_ACCESSORS)
                            .withRootObject(ConstraintUtils.getAttributeValueMap(constraint))
                            .build();
                    value = expr.getValue(ctx, attribute.getReturnType());
                } else {
                    String[] values = aattribute.value();
                    if (!(values.length == 1 && AAttribute.LOOK_REF.equals(values[0]))) {
                        Method attr = ConstraintUtils.requireAttribute(type, name);
                        value = ProcessUtils.parseValueString(attr.getReturnType(), values, 0);
                    } else if (AAttribute.DEFAULT.equals(aattribute.ref())) {
                        Method attr = ConstraintUtils.requireAttribute(type, name);
                        value = attr.getDefaultValue();
                    } else {
                        String ref = aattribute.ref();
                        if (ref.isEmpty()) { ref = name; }
                        value = ConstraintUtils.requireValue(constraint, ref);
                    }
                }
            } else if (ConstraintUtils.Attributes.MESSAGE.equals(name)) {
                String message = acombine.message();
                if (message.isEmpty()) { message = ConstraintUtils.getMessage(constraint); }
                value = message;
            } else {
                value = ConstraintUtils.getValue(constraint, name);
                if (value == null) {
                    value = attribute.getDefaultValue();
                    if (value == null) {
                        throw new ConstraintAnalysisException(
                                "%s on @%s is missing defined attribute[%s]",
                                acombine, constraint.annotationType().getSimpleName(), name);
                    }
                }
            }
            memberValues.put(name, value);
        });
        return ProcessUtils.proxyConstraint(type, memberValues);
    }

    private static final ExpressionParser SPEL_PARSER = new SpelExpressionParser();

    private static final PropertyAccessor[] PROPERTY_ACCESSORS = new PropertyAccessor[]{
            new MapAccessor(),
            DataBindingPropertyAccessor.forReadOnlyAccess(),
    };

    private static class MapAccessor implements PropertyAccessor {

        private static final Class<?>[] SPECIFIC_TARGET_CLASSES = new Class[]{Map.class};

        private MapAccessor() {}

        @Override
        public Class<?>[] getSpecificTargetClasses() {
            return SPECIFIC_TARGET_CLASSES;
        }

        @Override
        public boolean canRead(EvaluationContext context, Object target, String name) {
            return ((Map) target).containsKey(name);
        }

        @Override
        public TypedValue read(EvaluationContext context, Object target, String name) {
            return new TypedValue(((Map) target).get(name));
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
