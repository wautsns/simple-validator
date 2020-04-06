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

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;
import com.github.wautsns.simplevalidator.util.common.CollectionUtils;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
import com.github.wautsns.simplevalidator.util.extractor.ValueExtractor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Constraint metadata.
 *
 * @param <A> type of constraint
 * @author wautsns
 * @since Mar 21, 2020
 */
@Getter
public class ConstraintMetadata<A extends Annotation> {

    /** constraint attributes */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Attributes {

        /** attribute: message */
        public static final String MESSAGE = "message";
        /** attribute: order */
        public static final String ORDER = "order";

    }

    /** constraint type */
    private final Class<A> constraintType;
    /** order(only used to control the execution order of <strong>combined</strong> constraints) */
    private final int order;
    /** supported criterion factories */
    private final List<CriterionFactory<A, ?, ?>> criterionFactories;
    /** supported value extractors */
    private final List<ValueExtractor> valueExtractors;
    /** fixed combined constraints */
    private final List<Constraint<?>> fixedCombinedConstraints;
    /** dynamic combined constraint metadata list */
    private final List<DynamicCombinedConstraintMetadata<A, ?>> dynamicCombinedConstraintMetadataList;
    /** name -> attribute map */
    private final Map<String, Method> attributeMap;

    /**
     * Whether the constraint is only used to combine other constraints.
     *
     * @return {@code true} if the constraint type is only used to combine other constraints, otherwise {@code false}
     */
    public boolean isOnlyUsedToCombineOtherConstraints() {
        return criterionFactories == null;
    }

    /**
     * Require the attribute of the specified name.
     *
     * @param name attribute name
     * @return attribute
     * @throws ConstraintAnalysisException if the attribute is not declared
     */
    public Method requireAttribute(String name) {
        Method attribute = attributeMap.get(name);
        if (attribute != null) { return attribute; }
        throw new ConstraintAnalysisException("[%s] requires an attribute[%s].", constraintType, name);
    }

    /**
     * Get the attribute of the specified name.
     *
     * @param name attribute name
     * @return attribute, or {@code null} if the attribute is not declared
     */
    public Method getAttribute(String name) {
        return attributeMap.get(name);
    }

    // #################### instance ####################################################

    /** constraint type -> {@code ConstraintMetadata} instance map */
    @SuppressWarnings("rawtypes")
    private static final Map<Class, ConstraintMetadata> INSTANCE_MAP = new ConcurrentHashMap<>();

    /**
     * Get the {@code ConstraintMetadata} instance of the specified constraint type.
     *
     * @param constraintType constraint type
     * @param <A> type of constraint
     * @return the {@code ConstraintMetadata} instance of the specified constraint type
     */
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> ConstraintMetadata<A> getInstance(Class<? extends A> constraintType) {
        return INSTANCE_MAP.computeIfAbsent(constraintType, ConstraintMetadata::new);
    }

    // ==================== constructor =================================================

    /**
     * Construct a constraint metadata.
     *
     * @param constraintType constraint type
     */
    private ConstraintMetadata(Class<A> constraintType) {
        AConstraint aConstraint = requireAConstraint(constraintType);
        this.constraintType = constraintType;
        this.order = aConstraint.order();
        this.criterionFactories = getCriterionFactories(constraintType);
        this.valueExtractors = getValueExtractors(constraintType);
        this.fixedCombinedConstraints = Constraint.filterOutConstraints(constraintType.getDeclaredAnnotations());
        this.dynamicCombinedConstraintMetadataList = initDynamicCombinedConstraintMetadataList(
                constraintType, aConstraint);
        this.attributeMap = getAttributeMap(constraintType);
        check(this);
    }

    /**
     * Initialize dynamicCombinedConstraintMetadataList.
     *
     * @param aConstraint AConstraint
     * @param <A> type of constraint
     * @return dynamicCombinedConstraintMetadataList
     */
    private static <A extends Annotation> List<DynamicCombinedConstraintMetadata<A, ?>> initDynamicCombinedConstraintMetadataList(
            Class<A> targetConstraintType, AConstraint aConstraint) {
        List<DynamicCombinedConstraintMetadata<A, ?>> tmp = Arrays.stream(aConstraint.combines())
                .map(aCombine -> new DynamicCombinedConstraintMetadata<>(targetConstraintType, aCombine))
                .collect(Collectors.toCollection(LinkedList::new));
        return CollectionUtils.unmodifiableList(tmp);
    }

    /**
     * Check the metadata.
     *
     * @param metadata metadata
     * @throws ConstraintAnalysisException if the metadata is illegal
     */
    private static void check(ConstraintMetadata<?> metadata) {
        if (metadata.isOnlyUsedToCombineOtherConstraints()
                && metadata.fixedCombinedConstraints.isEmpty()
                && metadata.dynamicCombinedConstraintMetadataList.isEmpty()) {
            throw new ConstraintAnalysisException(
                    "[%s] has neither criterionFactories nor combined constraints.", metadata.constraintType);
        }
    }

    // #################### utils #######################################################

    // ==================== AConstraint =================================================

    /**
     * Whether the annotation type is a constraint type(i.e. an annotation type annotated with {@link AConstraint}).
     *
     * @param annotationType annotation type
     * @return {@code true} if the annotation type is a constraint type, otherwise {@code false}
     */
    public static boolean isConstraintType(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(AConstraint.class);
    }

    // #################### internal utils ##############################################

    // ==================== AConstraint =================================================

    /**
     * Require the {@link AConstraint} on the annotation type.
     *
     * @param annotationType annotation type
     * @return {@link AConstraint} on the annotation class
     * @throws ConstraintAnalysisException if the annotation type is not annotated with {@link AConstraint}
     */
    private static AConstraint requireAConstraint(Class<? extends Annotation> annotationType) {
        AConstraint aConstraint = annotationType.getAnnotation(AConstraint.class);
        if (aConstraint != null) { return aConstraint; }
        throw new ConstraintAnalysisException("[%s] is not annotated with [%s]", annotationType, AConstraint.class);
    }

    // ==================== attribute ===================================================

    /** non-attribute names */
    private static final Set<String> NON_ATTRIBUTE_NAMES = new HashSet<>(Arrays.asList(
            "annotationType", "hashCode", "equals", "toString"));

    /**
     * Get attribute map(unmodified) of the specified constraint type.
     *
     * @param constraintType constraint type
     * @return attribute map(unmodified) of the specified constraint type
     */
    private static Map<String, Method> getAttributeMap(Class<? extends Annotation> constraintType) {
        return CollectionUtils.unmodifiableMap(Arrays.stream(constraintType.getMethods())
                .filter(method -> !NON_ATTRIBUTE_NAMES.contains(method.getName()))
                .collect(Collectors.toMap(Method::getName, Function.identity())));
    }

    // ==================== criterion factories =========================================

    /**
     * Get criterion factories for the specified constraint type.
     *
     * @param constraintType constraint type
     * @param <A> type of the constraint
     * @return criterion factories for the specified constraint type, or {@code null} if the constraint is a constraint
     * used only to combine other constraints
     */
    private static <A extends Annotation> List<CriterionFactory<A, ?, ?>> getCriterionFactories(
            Class<A> constraintType) {
        Field field = ReflectionUtils.getDeclaredField(constraintType, "CRITERION_FACTORIES");
        return (field == null) ? null : ReflectionUtils.getValue(null, field);
    }

    // ==================== value extractors ============================================

    /**
     * Get value extractors for the specified constraint type.
     *
     * @param constraintType constraint type
     * @return value extractors for the specified constraint type
     */
    private static List<ValueExtractor> getValueExtractors(Class<? extends Annotation> constraintType) {
        Field field = ReflectionUtils.getDeclaredField(constraintType, "VALUE_EXTRACTORS");
        return (field == null) ? new LinkedList<>() : ReflectionUtils.getValue(null, field);
    }

}
