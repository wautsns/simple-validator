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
package com.github.wautsns.simplevalidator.model.constraint;

import com.github.wautsns.simplevalidator.constraint.AVariableAlias;
import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.exception.analysis.IllegalConstrainedNodeException;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.CollectionUtils;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
import com.github.wautsns.simplevalidator.util.extractor.ValueExtractor;
import com.github.wautsns.templatemessage.variable.Variable;
import com.github.wautsns.templatemessage.variable.VariableValueMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Constraint.
 *
 * @param <A> type of constraint
 * @author wautsns
 * @since Mar 21, 2020
 */
@Getter
public class Constraint<A extends Annotation> {

    /** constraint metadata */
    private final ConstraintMetadata<A> metadata;
    /** original constraint */
    private final A origin;
    /** attribute value map */
    private final Map<String, Object> attributeValueMap;
    /** combined constraints */
    private final List<Constraint<?>> combinedConstraints;
    /** variable value map */
    private final VariableValueMap variableValueMap;
    /** criterion processor */
    private final CriterionProcessor criterionProcessor;

    /**
     * Require message.
     *
     * @return message
     */
    public String requireMessage() {
        return requireValue(ConstraintMetadata.Attributes.MESSAGE);
    }

    /**
     * Get message.
     *
     * @return message, or {@code null} if message is not declared
     */
    public String getMessage() {
        return getValue(ConstraintMetadata.Attributes.MESSAGE);
    }

    /**
     * Get order.
     *
     * @return order, or {@code null} if order is not declared
     */
    public Integer getOrder() {
        return getValue(ConstraintMetadata.Attributes.ORDER);
    }

    /**
     * Require attribute value.
     *
     * @param name attribute name
     * @return attribute value
     * @throws ConstraintAnalysisException if the attribute is not declared
     */
    public <T> T requireValue(String name) {
        metadata.requireAttribute(name);
        return getValue(name);
    }

    /**
     * Get attribute value.
     *
     * @param name attribute name
     * @param <T> type of value
     * @return attribute value, or {@code null} if the attribute is not declared
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(String name) {
        return (T) attributeValueMap.get(name);
    }

    /**
     * Get attribute value.
     *
     * @param name attribute name
     * @param defaultValue default value
     * @param <T> type of value
     * @return attribute value, or default value if the attribute is not declared
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(String name, T defaultValue) {
        return (T) attributeValueMap.getOrDefault(name, defaultValue);
    }

    /**
     * Whether the constraint applies to the specified type(<strong>ignore value extractors</strong>).
     *
     * @param type type
     * @return {@code true} if the constraint applies to the specified type(<strong>ignore value extractors</strong>),
     * otherwise {@code false}
     */
    public boolean appliesTo(Type type) {
        if (metadata.isOnlyUsedToCombineOtherConstraints()) {
            return combinedConstraints.stream()
                    .allMatch(constraint -> constraint.appliesTo(type));
        } else {
            return metadata.getCriterionFactories().stream()
                    .anyMatch(criterionFactory -> criterionFactory.appliesTo(type, origin));
        }
    }

    /**
     * Require the value extractor suitable for the specified type.
     *
     * @param type type
     * @return the value extractor suitable for the specified type
     * @throws ConstraintAnalysisException if no value extractor applies to the specified type
     */
    public ValueExtractor requireApplicableValueExtractor(Type type) {
        ValueExtractor applicableValueExtractor = metadata.getValueExtractors().stream()
                .filter(valueExtractor -> valueExtractor.appliesTo(type))
                .findFirst()
                .orElse(null);
        if (applicableValueExtractor != null) { return applicableValueExtractor; }
        if (metadata.isOnlyUsedToCombineOtherConstraints()) {
            ValueExtractor ref = combinedConstraints.get(0).requireApplicableValueExtractor(type);
            boolean allMatch = combinedConstraints.stream()
                    .skip(1)
                    .map(constraint -> constraint.requireApplicableValueExtractor(type))
                    .allMatch(ref::equals);
            if (allMatch) { return ref; }
        }
        throw new ConstraintAnalysisException("There is no applicable value extractor for type[%s]", type);
    }

    // #################### instance ####################################################

    /** constraint type -> ((attribute -> value map) -> constraint instance) map */
    @SuppressWarnings("rawtypes")
    private static final Map<Class, Map<Map<String, Object>, Constraint>> INSTANCE_MAP = new ConcurrentHashMap<>();

    /**
     * Get {@code Constraint} instance.
     *
     * @param constraint constraint
     * @param <A> type of constraint
     * @return {@code Constraint} instance
     */
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> Constraint<A> getInstance(Annotation constraint) {
        Class<? extends Annotation> constraintType = constraint.annotationType();
        Map<String, Object> attributeValues = getAttributeValueMap(constraint);
        return INSTANCE_MAP
                .computeIfAbsent(constraintType, i -> new ConcurrentHashMap<>())
                .computeIfAbsent(attributeValues, i -> new Constraint<>(constraint));
    }

    // ==================== constructor =================================================

    /**
     * Construct a constraint.
     *
     * @param constraint constraint
     */
    @SuppressWarnings("unchecked")
    private Constraint(A constraint) {
        this.metadata = ConstraintMetadata.getInstance((Class<A>) constraint.annotationType());
        this.origin = constraint;
        this.attributeValueMap = getAttributeValueMap(constraint);
        this.combinedConstraints = initCombinedConstraints(this);
        this.variableValueMap = getVariableValueMap(constraint);
        this.criterionProcessor = new CriterionProcessor();
    }

    /**
     * Initialize combined constraints.
     *
     * @param constraint constraint
     * @return combined constraints
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List<Constraint<?>> initCombinedConstraints(Constraint<?> constraint) {
        ConstraintMetadata<?> metadata = ConstraintMetadata.getInstance(constraint.metadata.getConstraintType());
        List<Constraint<?>> combinedConstraints = new LinkedList<>();
        combinedConstraints.addAll(metadata.getFixedCombinedConstraints());
        metadata.getDynamicCombinedConstraintMetadataList().forEach(
                dccMetadata -> combinedConstraints.add(dccMetadata.generate((Constraint) constraint)));
        combinedConstraints.sort((c1, c2) -> ORDER_COMPARATOR.compare(c1.getOrder(), c2.getOrder()));
        return CollectionUtils.unmodifiableList(combinedConstraints);
    }

    // #################### utils #######################################################

    /** constraint order comparator */
    public static final Comparator<Integer> ORDER_COMPARATOR = (o1, o2) -> {
        if (o1 == null) {
            return (o2 == null) ? 0 : -1;
        } else {
            return (o2 == null) ? 1 : o1.compareTo(o2);
        }
    };

    // ==================== constraint ==================================================

    /**
     * Filter out constraints.
     *
     * @param annotations annotations
     * @return constraints(unmodified)
     */
    public static List<Constraint<?>> filterOutConstraints(Annotation[] annotations) {
        return filterOutConstraints(Arrays.asList(annotations));
    }

    /**
     * Filter out constraints.
     *
     * @param annotations annotation list
     * @return constraints(unmodified)
     */
    public static List<Constraint<?>> filterOutConstraints(List<Annotation> annotations) {
        List<Constraint<?>> constraints = annotations.stream()
                .filter(annotation -> ConstraintMetadata.isConstraintType(annotation.annotationType()))
                .map(Constraint::getInstance)
                .distinct()
                .collect(Collectors.toCollection(LinkedList::new));
        return CollectionUtils.unmodifiableList(constraints);
    }

    /**
     * Filter out constraint list.
     *
     * @param annotatedType annotated type
     * @return constraint list(unmodified)
     */
    public static List<Constraint<?>> filterOutConstraints(AnnotatedType annotatedType) {
        List<Constraint<?>> constraints = new LinkedList<>();
        constraints.addAll(filterOutConstraints(annotatedType.getDeclaredAnnotations()));
        Type type = annotatedType.getType();
        if (type instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            constraints.addAll(0, filterOutConstraints(typeVariable.getDeclaredAnnotations()));
            for (AnnotatedType annotatedBound : typeVariable.getAnnotatedBounds()) {
                constraints.addAll(0, filterOutConstraints(annotatedBound));
            }
        }
        return CollectionUtils.unmodifiableList(constraints.stream()
                .distinct()
                .collect(Collectors.toCollection(LinkedList::new)));
    }

    // ==================== criterion processor =========================================

    /** constraint criterion processor */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class CriterionProcessor {

        /**
         * Process criteria wip.
         *
         * @param node node
         * @param wip criteria wip
         */
        public void process(ConstrainedNode node, Criteria wip) {
            String message = getMessage();
            if (message != null) {
                Criteria tmp = Criteria.newInstance(node.getType());
                processWithoutVariables(node, tmp);
                Criterion criterion = tmp.simplify();
                if (criterion == null) { return; }
                VariableValueMap vvm = new VariableValueMap();
                vvm.put(variableValueMap);
                vvm.put(ValidationFailure.Variables.TARGET, node.getLocation());
                criterion = criterion.enhanceFailure(failure -> failure.setMessageTemplate(message).put(vvm));
                wip.add(criterion);
            } else if (metadata.isOnlyUsedToCombineOtherConstraints()) {
                combinedConstraints.forEach(constraint -> constraint.criterionProcessor.process(node, wip));
            } else {
                // e.g. VNullable
                processWithoutVariables(node, wip);
            }
        }

        /**
         * Process criteria wip without variables.
         *
         * <p>Runtime variables will still be added.
         *
         * @param node node
         * @param wip criteria wip
         */
        public void processWithoutVariables(ConstrainedNode node, Criteria wip) {
            int order = metadata.getOrder();
            Iterator<Constraint<?>> iterator = combinedConstraints.iterator();
            Constraint<?> current = null;
            while (iterator.hasNext()) {
                current = iterator.next();
                Integer combinedConstraintOrder = current.getOrder();
                if (combinedConstraintOrder == null || combinedConstraintOrder <= order) {
                    current.criterionProcessor.processWithoutVariables(node, wip);
                } else {
                    current = null;
                }
            }
            if (!metadata.isOnlyUsedToCombineOtherConstraints()) { processByCriterionFactories(node, wip); }
            if (current != null) { current.criterionProcessor.processWithoutVariables(node, wip); }
            while (iterator.hasNext()) { iterator.next().criterionProcessor.processWithoutVariables(node, wip); }
        }

        /**
         * Process criteria wip by criterion factories.
         *
         * @param node node
         * @param wip criteria wip
         */
        private void processByCriterionFactories(ConstrainedNode node, Criteria wip) {
            for (CriterionFactory criterionFactory : metadata.getCriterionFactories()) {
                if (criterionFactory.appliesTo(node.getType(), origin)) {
                    criterionFactory.process(node, origin, wip);
                    return;
                }
            }
            throw new IllegalConstrainedNodeException(node.getLocation(), origin);
        }

    }

    // #################### internal utils ##############################################

    // ==================== attribute value =============================================

    /**
     * Get the attribute value map(unmodified) of the specified constraint.
     *
     * @param constraint constraint
     * @return the attribute value map(unmodified) of the specified constraint
     */
    private static Map<String, Object> getAttributeValueMap(Annotation constraint) {
        InvocationHandler h = Proxy.getInvocationHandler(constraint);
        Field field = ReflectionUtils.requireDeclaredField(h.getClass(), "memberValues");
        Map<String, Object> memberValues = ReflectionUtils.getValue(h, field);
        return CollectionUtils.unmodifiableMap(memberValues);
    }

    // ==================== variable value map ==========================================

    /**
     * Get variable value map declared in the constraint.
     *
     * @param constraint constraint
     * @return variable value map declared in the constraint
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static VariableValueMap getVariableValueMap(Annotation constraint) {
        VariableValueMap variableValueMap = new VariableValueMap();
        ConstraintMetadata<?> metadata = ConstraintMetadata.getInstance(constraint.annotationType());
        for (Field field : metadata.getConstraintType().getFields()) {
            if (!Variable.class.isAssignableFrom(field.getType())) { continue;}
            Variable variable = ReflectionUtils.getValue(null, field);
            AVariableAlias variableAlias = field.getAnnotation(AVariableAlias.class);
            String attributeName = (variableAlias == null) ? variable.getName() : variableAlias.value();
            Method attribute = metadata.getAttribute(attributeName);
            if (attribute != null) { variableValueMap.put(variable, ReflectionUtils.invoke(constraint, attribute)); }
        }
        return variableValueMap;
    }

}
