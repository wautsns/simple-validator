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
package com.github.wautsns.simplevalidator.kernal.constraint;

import com.github.wautsns.simplevalidator.constraint.AAttribute;
import com.github.wautsns.simplevalidator.constraint.ACombine;
import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.util.common.AnnotationProxy;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.DataBindingPropertyAccessor;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Dynamic combined constraint metadata.
 *
 * @param <A> type of target constraint
 * @param <C> type of combined constraint
 * @author wautsns
 * @since Mar 21, 2020
 */
public class DynamicCombinedConstraintMetadata<A extends Annotation, C extends Annotation> {

    /** Target constraint type. */
    private final Class<A> targetConstraintType;
    /** ACombine. */
    private final ACombine aCombine;
    /** Fixed attribute value map. */
    private final Map<String, Object> fixedAttributeValueMap;
    /** Dynamic attribute value producer map. */
    private final Map<String, Function<Constraint<A>, Object>> dynamicAttributeValueProducerMap;

    /**
     * Generate constraint.
     *
     * @param targetConstraint target constraint
     * @return constraint
     */
    @SuppressWarnings("unchecked")
    public Constraint<C> generate(Constraint<A> targetConstraint) {
        HashMap<String, Object> attributeValueMap = new HashMap<>(dynamicAttributeValueProducerMap.size());
        dynamicAttributeValueProducerMap.forEach(
                (name, producer) -> attributeValueMap.put(name, producer.apply(targetConstraint)));
        check(targetConstraintType, aCombine, attributeValueMap);
        attributeValueMap.putAll(fixedAttributeValueMap);
        LinkedHashMap<String, Object> memberValues = new LinkedHashMap<>(attributeValueMap.size());
        Class<C> constraintType = (Class<C>) aCombine.constraint();
        ConstraintMetadata.getInstance(aCombine.constraint()).getAttributeMap().forEach(
                (name, i) -> memberValues.put(name, attributeValueMap.get(name)));
        return Constraint.getInstance(AnnotationProxy.proxy(constraintType, memberValues));
    }

    // #################### constructor #################################################

    /**
     * Construct a dynamic combined constraint metadata.
     *
     * @param targetConstraintType target constraint type
     * @param aCombine ACombine
     */
    public DynamicCombinedConstraintMetadata(Class<A> targetConstraintType, ACombine aCombine) {
        this.targetConstraintType = targetConstraintType;
        this.aCombine = aCombine;
        this.fixedAttributeValueMap = new HashMap<>(8);
        this.dynamicAttributeValueProducerMap = new HashMap<>(8);
        initAttributeValueMap(aCombine, fixedAttributeValueMap, dynamicAttributeValueProducerMap);
        check(targetConstraintType, aCombine, fixedAttributeValueMap);
    }

    /**
     * Initialize attribute value map.
     *
     * @param aCombine ACombine
     * @param fixedAttributeValueMap fixed attribute value map
     * @param dynamicAttributeValueMap dynamic attribute value map
     * @param <A> type of constraint
     */
    private static <A extends Annotation> void initAttributeValueMap(
            ACombine aCombine,
            Map<String, Object> fixedAttributeValueMap,
            Map<String, Function<Constraint<A>, Object>> dynamicAttributeValueMap) {
        fixedAttributeValueMap.put(ConstraintMetadata.Attributes.ORDER, aCombine.order());
        ConstraintMetadata<?> metadata = ConstraintMetadata.getInstance(aCombine.constraint());
        Map<String, AAttribute> aAttributes = Arrays.stream(aCombine.attributes())
                .collect(Collectors.toMap(AAttribute::name, Function.identity()));
        metadata.getAttributeMap().forEach((name, attribute) -> {
            AAttribute aAttribute = aAttributes.get(name);
            Object defaultValue = attribute.getDefaultValue();
            if (aAttribute != null) {
                processAAttribute(aAttribute, attribute, fixedAttributeValueMap, dynamicAttributeValueMap);
            } else if (ConstraintMetadata.Attributes.MESSAGE.equals(name)) {
                String message = aCombine.message();
                fixedAttributeValueMap.put(name, message.isEmpty() ? defaultValue : message);
            } else {
                dynamicAttributeValueMap.put(name, constraint -> constraint.getValue(name, defaultValue));
            }
        });
    }

    /**
     * Process AAttribute.
     *
     * @param aAttribute AAttribute
     * @param attribute attribute
     * @param fixedAttributeValueMap fixed attribute value map
     * @param dynamicAttributeValueMap dynamic attribute value map
     * @param <A> type of constraint
     */
    private static <A extends Annotation> void processAAttribute(
            AAttribute aAttribute, Method attribute,
            Map<String, Object> fixedAttributeValueMap,
            Map<String, Function<Constraint<A>, Object>> dynamicAttributeValueMap) {
        String name = attribute.getName();
        String spel = aAttribute.spel();
        String[] values = aAttribute.values();
        String ref = aAttribute.ref();
        Class<?> type = attribute.getReturnType();
        if (!AAttribute.LOOK_VALUE.equals(spel)) {
            dynamicAttributeValueMap.put(name, parseValueSpel(type, spel));
        } else if (!(values.length == 1 && AAttribute.LOOK_REF.equals(values[0]))) {
            fixedAttributeValueMap.put(name, parseValueStrings(type, values, 0));
        } else if (AAttribute.DEFAULT.equals(ref)) {
            fixedAttributeValueMap.put(name, attribute.getDefaultValue());
        } else if (ref.isEmpty()) {
            dynamicAttributeValueMap.put(name, constraint -> constraint.requireValue(name));
        } else {
            dynamicAttributeValueMap.put(name, constraint -> constraint.requireValue(ref));
        }
    }

    /**
     * Check attribute value map.
     *
     * @param targetConstraintType target constraint type
     * @param aCombine ACombine
     * @param attributeValueMap attribute value map
     */
    private static void check(
            Class<?> targetConstraintType, ACombine aCombine, Map<String, Object> attributeValueMap) {
        for (Map.Entry<String, Object> entry : attributeValueMap.entrySet()) {
            if (entry.getValue() == null) {
                throw new ConstraintAnalysisException(
                        "Value of attribute[%s] of combined constraint[%s] defined in [%s] is null.",
                        entry.getKey(), aCombine, targetConstraintType);
            }
        }
    }

    // #################### value utils #################################################

    // ==================== value strings ===============================================

    /**
     * Parse value strings.
     *
     * @param type target type
     * @param valueStrings value strings.
     * @param index index(only valid when type is not array)
     * @return value
     */
    private static Object parseValueStrings(Class<?> type, String[] valueStrings, int index) {
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            Object array = Array.newInstance(componentType, valueStrings.length);
            for (int i = 0; i < valueStrings.length; i++) {
                Object value = parseValueStrings(type, valueStrings, i);
                Array.set(array, i, value);
            }
            return array;
        } else if (type == String.class) {
            return valueStrings[index];
        } else if (type == int.class) {
            return Integer.valueOf(valueStrings[index]);
        } else if (type == boolean.class) {
            return Boolean.valueOf(valueStrings[index]);
        } else if (type == Class.class) {
            return ReflectionUtils.requireClass(valueStrings[index]);
        } else if (type == long.class) {
            return Long.valueOf(valueStrings[index]);
        } else if (type == byte.class) {
            return Byte.valueOf(valueStrings[index]);
        } else if (type == char.class) {
            return valueStrings[index].charAt(0);
        } else if (type == short.class) {
            return Short.valueOf(valueStrings[index]);
        } else if (type == double.class) {
            return Double.valueOf(valueStrings[index]);
        } else if (type == float.class) {
            return Float.valueOf(valueStrings[index]);
        } else {
            throw new IllegalStateException();
        }
    }

    // ==================== value spel ==================================================

    /**
     * Parse value spel.
     *
     * @param type target type
     * @param spel spel
     * @param <A> type of target constraint
     * @return value producer
     */
    private static <A extends Annotation> Function<Constraint<A>, Object> parseValueSpel(Class<?> type, String spel) {
        Expression expr = SPEL_PARSER.parseExpression(spel);
        return constraint -> {
            SimpleEvaluationContext ctx = SimpleEvaluationContext
                    .forPropertyAccessors(PROPERTY_ACCESSORS)
                    .withRootObject(constraint.getAttributeValueMap())
                    .build();
            return expr.getValue(ctx, type);
        };
    }

    /** Spel parser. */
    private static final ExpressionParser SPEL_PARSER = new SpelExpressionParser();

    /** Property accessors. */
    private static final PropertyAccessor[] PROPERTY_ACCESSORS = new PropertyAccessor[]{
            MapAccessor.INSTANCE, DataBindingPropertyAccessor.forReadOnlyAccess() };

    /** Map accessor. */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class MapAccessor implements PropertyAccessor {

        public static final MapAccessor INSTANCE = new MapAccessor();

        /** Specific target classes. */
        private static final Class<?>[] SPECIFIC_TARGET_CLASSES = new Class[]{ Map.class };

        @Override
        public Class<?>[] getSpecificTargetClasses() {
            return SPECIFIC_TARGET_CLASSES;
        }

        @Override
        public boolean canRead(EvaluationContext context, Object target, String name) {
            return ((Map<?, ?>) target).containsKey(name);
        }

        @Override
        public TypedValue read(EvaluationContext context, Object target, String name) {
            return new TypedValue(((Map<?, ?>) target).get(name));
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
