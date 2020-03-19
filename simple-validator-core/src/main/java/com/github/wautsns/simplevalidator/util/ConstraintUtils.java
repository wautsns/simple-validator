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
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;
import com.github.wautsns.simplevalidator.util.common.CollectionUtils;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
import com.github.wautsns.templatemessage.variable.Variable;
import com.github.wautsns.templatemessage.variable.VariableValueMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Constraint utils.
 *
 * @author wautsns
 * @since Mar 12, 2020
 */
@UtilityClass
public class ConstraintUtils {

    // -------------------- AConstraint -------------------------------------------------

    /**
     * Require {@code AConstraint} value on the constraint annotation class.
     *
     * @param constraintClass constraint class
     * @return {@code AConstraint} value on the constraint class
     * @throws ConstraintAnalysisException if there is no {@code AConstraint} on the constraint class.
     */
    public static AConstraint requireAConstraint(Class<? extends Annotation> constraintClass) {
        AConstraint aConstraint = getAConstraint(constraintClass);
        if (aConstraint != null) { return aConstraint; }
        throw new ConstraintAnalysisException("%s is missing %s", constraintClass, AConstraint.class);
    }

    /**
     * Get {@code AConstraint} value on the constraint annotation class.
     *
     * @param constraintClass constraint class
     * @return {@code AConstraint} value on the constraint class, or {@code null} if there is no {@code AConstraint}
     * annotation on the constraint class
     */
    public static AConstraint getAConstraint(Class<? extends Annotation> constraintClass) {
        return constraintClass.getAnnotation(AConstraint.class);
    }

    // -------------------- constraint --------------------------------------------------

    /**
     * Whether the annotation is constraint(i.e. annotated with {@linkplain AConstraint @AConstraint}).
     *
     * @param annotation annotation
     * @return {@code true} if the annotation is constraint, otherwise {@code false}
     */
    public static boolean isConstraint(Annotation annotation) {
        return annotation.annotationType().isAnnotationPresent(AConstraint.class);
    }

    /**
     * Filter out constraints.
     *
     * @param annotations annotations
     * @return constraints(unmodified)
     */
    public static List<Annotation> filterOutConstraints(Annotation[] annotations) {
        return filterOutConstraints(Arrays.asList(annotations));
    }

    /**
     * Filter out constraints.
     *
     * @param annotations annotations
     * @return constraints(unmodified)
     */
    public static List<Annotation> filterOutConstraints(List<Annotation> annotations) {
        List<Annotation> constraints = annotations.stream()
                .filter(ConstraintUtils::isConstraint)
                .collect(Collectors.toCollection(LinkedList::new));
        if (constraints.equals(annotations)) { constraints = annotations; }
        return CollectionUtils.unmodifiableList(constraints);
    }

    // -------------------- attribute ---------------------------------------------------

    /** constraint attributes */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Attributes {

        /** attribute: message */
        public static final String MESSAGE = "message";
        /** attribute: order */
        public static final String ORDER = "order";

    }

    /**
     * Require the attribute.
     *
     * @param constraintClass constraint class.
     * @param name attribute name
     * @return constraint attribute
     * @throws ConstraintAnalysisException if the attribute does not exist
     */
    public static Method requireAttribute(Class<? extends Annotation> constraintClass, String name) {
        Method attribute = getAttribute(constraintClass, name);
        if (attribute != null) { return attribute; }
        throw new ConstraintAnalysisException("%s is missing attribute[%s].", constraintClass, name);
    }

    /**
     * Get constraint attribute.
     *
     * @param constraintClass constraint class.
     * @param name attribute name
     * @return constraint attribute, or {@code null} if the attribute does not exist
     */
    public static Method getAttribute(Class<? extends Annotation> constraintClass, String name) {
        return ReflectionUtils.getDeclaredMethod(constraintClass, name);
    }

    /** non-attribute method names */
    private static final Set<String> NOT_ATTRIBUTE_NAMES = new HashSet<>(Arrays.asList(
            "annotationType", "hashCode", "equals", "toString"));

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

    // -------------------- attribute value ---------------------------------------------

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
     * Get attribute value map.
     *
     * @param constraint constraint
     * @return attribute value map
     */
    public static Map<String, Object> getAttributeValueMap(Annotation constraint) {
        InvocationHandler h = Proxy.getInvocationHandler(constraint);
        Field field = ReflectionUtils.requireDeclaredField(h.getClass(), "memberValues");
        return ReflectionUtils.getValue(h, field);
    }

    // -------------------- criterion factories -----------------------------------------

    /**
     * Get criterion factories of the constraint.
     *
     * @param constraintClass constraint class
     * @param <A> type of the constraint
     * @return criterion factories of the constraint, or empty list(unmodified) if the constraint is a combined
     * constraint
     */
    public static <A extends Annotation> List<CriterionFactory<A, ?, ?>> getCriterionFactories(
            Class<A> constraintClass) {
        Field field = ReflectionUtils.getDeclaredField(constraintClass, "CRITERION_FACTORIES");
        return (field == null) ? Collections.emptyList() : ReflectionUtils.getValue(null, field);
    }

    // -------------------- variables ---------------------------------------------------

    /**
     * Get variable value map defined the constraint.
     *
     * @param constraint constraint
     * @return variable value map
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static VariableValueMap getVariableValueMap(Annotation constraint) {
        VariableValueMap variableValueMap = new VariableValueMap();
        Class<?> constraintClass = constraint.annotationType();
        Arrays.stream(constraintClass.getFields())
                .filter(field -> Variable.class.isAssignableFrom(field.getType()))
                .forEach(variableField -> {
                    Variable variable = ReflectionUtils.getValue(null, variableField);
                    AVariableAlias variableAlias = variableField.getAnnotation(AVariableAlias.class);
                    String name = (variableAlias == null) ? variable.getName() : variableAlias.value();
                    Object value = getValue(constraint, name);
                    if (value != null) { variableValueMap.put(variable, value); }
                });
        return variableValueMap;
    }

}
