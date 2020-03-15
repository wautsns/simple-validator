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
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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

    // -------------------- constraint --------------------------------------------------

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
        AConstraint aConstraint = getAConstraint(constraintClass);
        if (aConstraint != null) { return aConstraint; }
        throw new ConstraintAnalysisException("%s is missing %s", constraintClass, AConstraint.class);
    }

    /**
     * Get {@code AConstraint} value on the constraint annotation class.
     *
     * @param constraintClass constraint annotation class
     * @return {@code AConstraint} value on the constraint annotation class, or {@code null} if there is no {@code
     * AConstraint} annotation on the constraint class
     */
    public static AConstraint getAConstraint(Class<? extends Annotation> constraintClass) {
        return constraintClass.getAnnotation(AConstraint.class);
    }

    /**
     * Filter out constraints.
     *
     * @param annotations annotations
     * @return constraints
     */
    public static List<Annotation> filterOutConstraints(Annotation[] annotations) {
        return filterOutConstraints(Arrays.asList(annotations));
    }

    /**
     * Filter out constraints.
     *
     * @param annotations annotations
     * @return constraints
     */
    public static List<Annotation> filterOutConstraints(List<Annotation> annotations) {
        return CollectionUtils.unmodifiableList(annotations.stream()
                .filter(ConstraintUtils::isConstraint)
                .collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     * Get indexes constraints.
     *
     * @param annotatedType annotated type
     * @return indexes constraints
     */
    public static Map<List<Short>, List<Annotation>> getIndexesConstraints(AnnotatedType annotatedType) {
        Map<List<Short>, List<Annotation>> indexesAnnotations = IndexesAnnotationsUtils.resolve(annotatedType);
        HashMap<List<Short>, List<Annotation>> indexesConstraints = new HashMap<>(8);
        indexesAnnotations.forEach((indexes, annotations) -> {
            List<Annotation> constraints = filterOutConstraints(annotations);
            if (!constraints.isEmpty()) { indexesConstraints.put(indexes, constraints); }
        });
        return indexesConstraints;
    }

    /** Indexes annotations utils. */
    @UtilityClass
    private static class IndexesAnnotationsUtils {

        /** class: AnnotatedTypeBaseImpl */
        private static final Class<?> ANNOTATED_TYPE_BASE_IMPL = ReflectionUtils.requireClass(
                "sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl");
        /** field: AnnotatedTypeFactory$AnnotatedTypeBaseImpl#allOnSameTargetTypeAnnotations */
        private static final Field ALL_ON_SAME_TARGET_TYPE_ANNOTATIONS = ReflectionUtils.requireDeclaredField(
                ReflectionUtils.requireClass("sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl"),
                "allOnSameTargetTypeAnnotations");
        /** field: TypeAnnotation#annotation */
        private static final Field ANNOTATION_OF_TYPE_ANNOTATION = ReflectionUtils.requireDeclaredField(
                ReflectionUtils.requireClass("sun.reflect.annotation.TypeAnnotation"), "annotation");
        /** field: TypeAnnotation#loc */
        private static final Field LOC_OF_TYPE_ANNOTATION = ReflectionUtils.requireDeclaredField(
                ReflectionUtils.requireClass("sun.reflect.annotation.TypeAnnotation"), "loc");
        /** field: TypeAnnotation$LocationInfo#locations */
        private static final Field LOCATIONS_OF_LOCATION_INFO = ReflectionUtils.requireDeclaredField(
                ReflectionUtils.requireClass("sun.reflect.annotation.TypeAnnotation$LocationInfo"), "locations");
        /** field: TypeAnnotation$LocationInfo$Location#index */
        private static final Field INDEX_OF_LOCATION = ReflectionUtils.requireDeclaredField(
                ReflectionUtils.requireClass("sun.reflect.annotation.TypeAnnotation$LocationInfo$Location"), "index");

        /**
         * Resolve annotatedType and return indexes annotation map.
         *
         * @param annotatedType an annotated type
         * @return indexes annotation map
         */
        public static Map<List<Short>, List<Annotation>> resolve(AnnotatedType annotatedType) {
            if (annotatedType.getClass() == ANNOTATED_TYPE_BASE_IMPL) {
                List<Short> indexes = Collections.emptyList();
                List<Annotation> annotations = Arrays.asList(annotatedType.getAnnotations());
                return Collections.singletonMap(indexes, annotations);
            }
            Map<List<Short>, List<Annotation>> indexesConstraints = new LinkedHashMap<>();
            Object allOnSameTargetTypeAnnotations = ReflectionUtils.getValue(
                    annotatedType, ALL_ON_SAME_TARGET_TYPE_ANNOTATIONS);
            for (int i = 0, l = Array.getLength(allOnSameTargetTypeAnnotations); i < l; i++) {
                Object typeAnnotation = Array.get(allOnSameTargetTypeAnnotations, i);
                resolve(indexesConstraints, typeAnnotation);
            }
            indexesConstraints.entrySet().forEach(e -> e.setValue(CollectionUtils.unmodifiableList(e.getValue())));
            return indexesConstraints;
        }

        /**
         * Resolve type annotation.
         *
         * @param indexesAnnotations an indexes constraints map
         * @param typeAnnotation type annotation
         */
        private static void resolve(Map<List<Short>, List<Annotation>> indexesAnnotations, Object typeAnnotation) {
            Annotation annotation = ReflectionUtils.getValue(typeAnnotation, ANNOTATION_OF_TYPE_ANNOTATION);
            Object loc = ReflectionUtils.getValue(typeAnnotation, LOC_OF_TYPE_ANNOTATION);
            Object locations = ReflectionUtils.getValue(loc, LOCATIONS_OF_LOCATION_INFO);
            int locationsLength = Array.getLength(locations);
            List<Short> indexes = new ArrayList<>(locationsLength);
            for (int j = 0; j < locationsLength; j++) {
                Object location = Array.get(locations, j);
                short index = ReflectionUtils.getShort(location, INDEX_OF_LOCATION);
                indexes.add(index);
            }
            indexesAnnotations.computeIfAbsent(indexes, i -> new LinkedList<>()).add(annotation);
        }

    }

    // -------------------- attributes --------------------------------------------------

    /** constraint attributes */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Attributes {

        /** attribute: message */
        public static final String MESSAGE = "message";
        /** attribute: order */
        public static final String ORDER = "order";

    }

    /**
     * Require the attribute named the specific name.
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

    /** not attribute method names */
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
     * @return attribute-value map
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
     * Get variable value map in the constraint.
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
                    Variable variable = ReflectionUtils.getValue(null, variableField);
                    AVariableAlias variableAlias = variableField.getAnnotation(AVariableAlias.class);
                    String name = (variableAlias == null) ? variable.getName() : variableAlias.value();
                    variableValueMap.put(variable, getValue(constraint, name));
                });
        return variableValueMap;
    }

}
