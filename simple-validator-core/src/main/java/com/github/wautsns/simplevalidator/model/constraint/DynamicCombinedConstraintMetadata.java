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

import com.github.wautsns.simplevalidator.constraint.AAttribute;
import com.github.wautsns.simplevalidator.constraint.ACombine;
import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.util.common.AnnotationProxy;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
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
 * @param <A> target constraint type
 * @param <C> combined constraint type
 * @author wautsns
 * @since Mar 21, 2020
 */
public class DynamicCombinedConstraintMetadata<A extends Annotation, C extends Annotation> {

    /** target constraint type */
    private final Class<A> targetConstraintType;
    /** ACombine */
    private final ACombine aCombine;
    /** fixed attribute value map */
    private final Map<String, Object> fixedAttributeValueMap;
    /** dynamic attribute value producer map */
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
     * @param <A> constraint type
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
     * @param <A> constraint type
     */
    private static <A extends Annotation> void processAAttribute(
            AAttribute aAttribute, Method attribute,
            Map<String, Object> fixedAttributeValueMap,
            Map<String, Function<Constraint<A>, Object>> dynamicAttributeValueMap) {
        String name = attribute.getName();
        String spel = aAttribute.spel();
        String[] values = aAttribute.values();
        String ref = aAttribute.ref();
        if (!AAttribute.LOOK_VALUE.equals(spel)) {
            Expression expr = SPEL_PARSER.parseExpression(spel);
            dynamicAttributeValueMap.put(name, constraint -> {
                SimpleEvaluationContext ctx = SimpleEvaluationContext
                        .forPropertyAccessors(PROPERTY_ACCESSORS)
                        .withRootObject(constraint.getAttributeValueMap())
                        .build();
                return expr.getValue(ctx, attribute.getReturnType());
            });
        } else if (!(values.length == 1 && AAttribute.LOOK_REF.equals(values[0]))) {
            fixedAttributeValueMap.put(name, parseValueStrings(attribute.getReturnType(), values, 0));
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
                        "Attribute[%s] value of combined constraint[%s] defined in [%s] is null.",
                        entry.getKey(), aCombine, targetConstraintType);
            }
        }
    }

    // #################### value ##############################################

    // ==================== value strings ===============================================

    /**
     * Parse value string.
     *
     * @param valueClass target class
     * @param valueStrings value strings.
     * @param index current index
     * @return value
     */
    private static Object parseValueStrings(Class<?> valueClass, String[] valueStrings, int index) {
        if (valueClass.isArray()) {
            Class<?> componentType = valueClass.getComponentType();
            Object array = Array.newInstance(componentType, valueStrings.length);
            for (int i = 0; i < valueStrings.length; i++) {
                Object value = parseValueStrings(valueClass, valueStrings, i);
                Array.set(array, i, value);
            }
            return array;
        } else if (valueClass == String.class) {
            return valueStrings[index];
        } else if (valueClass == int.class) {
            return Integer.valueOf(valueStrings[index]);
        } else if (valueClass == boolean.class) {
            return Boolean.valueOf(valueStrings[index]);
        } else if (valueClass == Class.class) {
            return ReflectionUtils.requireClass(valueStrings[index]);
        } else if (valueClass == long.class) {
            return Long.valueOf(valueStrings[index]);
        } else if (valueClass == byte.class) {
            return Byte.valueOf(valueStrings[index]);
        } else if (valueClass == char.class) {
            return valueStrings[index].charAt(0);
        } else if (valueClass == short.class) {
            return Short.valueOf(valueStrings[index]);
        } else if (valueClass == double.class) {
            return Double.valueOf(valueStrings[index]);
        } else if (valueClass == float.class) {
            return Float.valueOf(valueStrings[index]);
        } else {
            throw new IllegalStateException();
        }
    }

    // ==================== value spel ===============================================

    /** spel parser */
    private static final ExpressionParser SPEL_PARSER = new SpelExpressionParser();

    /** property accessors */
    private static final PropertyAccessor[] PROPERTY_ACCESSORS = new PropertyAccessor[]{
            new MapAccessor(), DataBindingPropertyAccessor.forReadOnlyAccess()};

    /** map accessor */
    private static class MapAccessor implements PropertyAccessor {

        private static final Class<?>[] SPECIFIC_TARGET_CLASSES = new Class[]{Map.class};

        private MapAccessor() {}

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
