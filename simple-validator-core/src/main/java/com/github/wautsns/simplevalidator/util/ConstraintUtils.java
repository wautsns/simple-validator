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
package com.github.wautsns.simplevalidator.util;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.constraint.AVariableAlias;
import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.util.normal.CollectionUtils;
import com.github.wautsns.simplevalidator.util.normal.ReflectionUtils;
import com.github.wautsns.templatemessage.variable.Variable;
import com.github.wautsns.templatemessage.variable.VariableValueMap;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Constraint utils.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@UtilityClass
public class ConstraintUtils {

    /** constraint attributes */
    public static class Attributes {

        /** attribute: message */
        public static final String MESSAGE = "message";
        /** attribute: order */
        public static final String ORDER = "order";

        private Attributes() {}

    }

    /**
     * Return {@code true} if the annotation is constraint. otherwise {@code false}.
     *
     * @param annotation annotation
     * @return {@code true} if the annotation is constraint. otherwise {@code false}
     */
    public static boolean isConstraint(Annotation annotation) {
        return annotation.annotationType().isAnnotationPresent(AConstraint.class);
    }

    /**
     * Require {@code AConstraint} value on the constraint annotation class.
     *
     * @param constraintClass constraint annotation class
     * @return {@code AConstraint} value on the constraint annotation class
     * @throws ConstraintAnalysisException if there is no {@code AConstraint} annotation on the constraint class.
     */
    public static AConstraint requireAConstraint(Class<? extends Annotation> constraintClass) {
        AConstraint aconstraint = getAConstraint(constraintClass);
        if (aconstraint != null) { return aconstraint; }
        throw new ConstraintAnalysisException(
                "@%s is not a constraint.(missing @%s)",
                constraintClass.getSimpleName(), AConstraint.class.getSimpleName());
    }

    /**
     * Get {@code AConstraint} value on the constraint annotation class.
     *
     * @param constraintClass constraint annotation class
     * @return {@code AConstraint} value on the constraint annotation class, or {@code null} if there is no {@code
     * AConstraint} annotation on the constraint class
     */
    private static AConstraint getAConstraint(Class<? extends Annotation> constraintClass) {
        return constraintClass.getAnnotation(AConstraint.class);
    }

    /**
     * Require constraint attribute.
     *
     * @param constraintClass constraint class.
     * @param name attribute name
     * @return constraint attribute
     * @throws ConstraintAnalysisException if the attribute does not exist
     */
    public static Method requireAttribute(Class<? extends Annotation> constraintClass, String name) {
        Method attribute = getAttribute(constraintClass, name);
        if (attribute != null) { return attribute; }
        throw new ConstraintAnalysisException(
                "Constraint @%s is missing attribute[%s].",
                constraintClass.getSimpleName(), name);
    }

    /**
     * Get constraint attribute.
     *
     * @param constraintClass constraint class.
     * @param name attribute name
     * @return constraint attribute, or {@code null} if the attribute does not exist
     */
    public static Method getAttribute(Class<? extends Annotation> constraintClass, String name) {
        return ReflectionUtils.accessMethod(constraintClass, name);
    }

    /** not attribute method names */
    private static final Set<String> NOT_ATTRIBUTE_NAMES = new HashSet<>(
            Arrays.asList("annotationType", "hashCode", "equals", "toString"));

    /**
     * Get all attributes of the constraint class.
     *
     * @param constraintClass constraint class
     * @return all attributes of the constraint class
     */
    public static List<Method> getAttributes(Class<? extends Annotation> constraintClass) {
        return Arrays.stream(constraintClass.getMethods())
                .filter(method -> !NOT_ATTRIBUTE_NAMES.contains(method.getName()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get constraints int annotations.
     *
     * @param annotations annotations
     * @return constraints
     */
    public static List<Annotation> getConstraints(Annotation[] annotations) {
        List<Annotation> tmp = Arrays.stream(annotations)
                .filter(ConstraintUtils::isConstraint)
                .collect(Collectors.toCollection(LinkedList::new));
        return CollectionUtils.unmodifiableList(tmp);
    }

    /**
     * Get value of attribute `message`.
     *
     * @param constraint constraint
     * @return value of attribute `message`
     */
    public static String getMessage(Annotation constraint) {
        return getValue(constraint, Attributes.MESSAGE);
    }

    /**
     * Get value of attribute `order`.
     *
     * @param constraint constraint
     * @return value of attribute `order`
     */
    public static Integer getOrder(Annotation constraint) {
        return getValue(constraint, Attributes.ORDER);
    }

    /**
     * Compare orderA and orderB.
     *
     * <p>{@code null} is equal to {@code null}, less than any others.
     *
     * @param orderA order A(maybe {@code null})
     * @param orderB order B(maybe {@code null})
     * @return {@code 1} if orderA is greater than orderB, {@code -1} if orderA is less than orderB, otherwise {@code 0}
     */
    public static int compareOrder(Integer orderA, Integer orderB) {
        if (orderA == null) {
            return (orderB == null) ? 0 : 1;
        } else if (orderB == null) {
            return 1;
        } else {
            return orderA.compareTo(orderB);
        }
    }

    /**
     * Require value of the attribute.
     *
     * @param constraint constraint
     * @param name name of attribute
     * @return value of the attribute
     * @throws ConstraintAnalysisException if the attribute does not exist
     */
    public static <T> T requireValue(Annotation constraint, String name) {
        return ReflectionUtils.invoke(constraint, requireAttribute(constraint.annotationType(), name));
    }

    /**
     * Get value of the attribute.
     *
     * @param constraint constraint
     * @param name name of attribute
     * @return value of the attribute, or {@code null} if the attribute does not exist
     */
    public static <T> T getValue(Annotation constraint, String name) {
        Method attribute = getAttribute(constraint.annotationType(), name);
        return (attribute == null) ? null : ReflectionUtils.invoke(constraint, attribute);
    }

    /**
     * Get attribute-value map.
     *
     * @param constraint constraint
     * @return attribute-value map
     */
    public static Map<String, Object> getAttributeValueMap(Annotation constraint) {
        InvocationHandler h = Proxy.getInvocationHandler(constraint);
        Field field = ReflectionUtils.accessField(h.getClass(), "memberValues");
        return ReflectionUtils.getFieldValue(h, Objects.requireNonNull(field));
    }

    /**
     * Get variable-value map in the constraint.
     *
     * @param constraint constraint
     * @return variable-value map
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static VariableValueMap getVariableValueMap(Annotation constraint) {
        VariableValueMap variableValueMap = new VariableValueMap();
        Class<?> constraintClass = constraint.annotationType();
        Arrays.stream(constraintClass.getFields())
                .filter(field -> Variable.class.isAssignableFrom(field.getType()))
                .forEach(variableField -> {
                    Variable variable = ReflectionUtils.getFieldValue(null, variableField);
                    AVariableAlias variableAlias = variableField.getAnnotation(AVariableAlias.class);
                    String name = (variableAlias == null) ? variable.getName() : variableAlias.value();
                    if (name.isEmpty()) { return; }
                    Method attribute = ReflectionUtils.accessMethod(constraintClass, name);
                    if (attribute == null) { return; }
                    Object value = ReflectionUtils.invoke(constraint, attribute);
                    variableValueMap.put(variable, value);
                });
        return variableValueMap;
    }

}
