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
 * @param <A> constraint type
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
    /** supported criterion factory list */
    private final List<CriterionFactory<A, ?, ?>> criterionFactoryList;
    /** supported value extractor list */
    private final List<ValueExtractor> valueExtractorList;
    /** fixed combined constraint list */
    private final List<Constraint<?>> fixedCombinedConstraintList;
    /** dynamic combined constraint metadata list */
    private final List<DynamicCombinedConstraintMetadata<A, ?>> dynamicCombinedConstraintMetadataList;
    /** name -> attribute map */
    private final Map<String, Method> attributeMap;

    /**
     * Whether the constraint type is only used to combine other constraints.
     *
     * @return {@code true} if the constraint type is only used to combine other constraints, otherwise false
     */
    public boolean isOnlyUsedToCombineOtherConstraints() {
        return criterionFactoryList == null;
    }

    /**
     * Require the attribute of the specific name.
     *
     * @param name attribute name
     * @return attribute
     * @throws ConstraintAnalysisException if the attribute does not exist
     */
    public Method requireAttribute(String name) {
        Method attribute = attributeMap.get(name);
        if (attribute != null) { return attribute; }
        throw new ConstraintAnalysisException("%s requires an attribute[%s].", constraintType, name);
    }

    /**
     * Get the attribute of the specific name.
     *
     * @param name attribute name
     * @return attribute, or {@code null} if the attribute does not exist
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
     * @param <A> constraint type
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
        this.criterionFactoryList = getCriterionFactoryList(constraintType);
        this.valueExtractorList = getValueExtractorList(constraintType);
        this.fixedCombinedConstraintList = Constraint.filterOutConstraintList(constraintType.getDeclaredAnnotations());
        this.dynamicCombinedConstraintMetadataList = initDynamicCombinedConstraintMetadataList(
                constraintType, aConstraint);
        this.attributeMap = getAttributeMap(constraintType);
        check(this);
    }

    /**
     * Initialize dynamicCombinedConstraintMetadataList.
     *
     * @param aConstraint AConstraint
     * @param <A> constraint type annotated the {@code AConstraint}.
     * @return dynamicCombinedConstraintMetadataList
     */
    private static <A extends Annotation> List<DynamicCombinedConstraintMetadata<A, ?>> initDynamicCombinedConstraintMetadataList(
            Class<A> targetConstraintType, AConstraint aConstraint) {
        return CollectionUtils.unmodifiableList(Arrays.stream(aConstraint.combines())
                .map(aCombine -> new DynamicCombinedConstraintMetadata<>(targetConstraintType, aCombine))
                .collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     * Check the metadata.
     *
     * @param metadata metadata
     * @throws ConstraintAnalysisException if the metadata is illegal
     */
    private static void check(ConstraintMetadata<?> metadata) {
        if (metadata.isOnlyUsedToCombineOtherConstraints()) {
            if (metadata.fixedCombinedConstraintList.isEmpty()
                    && metadata.dynamicCombinedConstraintMetadataList.isEmpty()) {
                throw new ConstraintAnalysisException(
                        "%s has neither criterionFactories nor combined constraints.", metadata.constraintType);
            }
        }
    }

    // #################### utils #######################################################

    // ==================== AConstraint =================================================

    /**
     * Whether the annotation type is constraint type(i.e. an annotation type annotated with {@link AConstraint}).
     *
     * @param annotationType annotation type
     * @return {@code true} if the annotation type is constraint type, otherwise {@code false}
     */
    public static boolean isConstraintType(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(AConstraint.class);
    }

    // #################### internal utils ##############################################

    // ==================== AConstraint =================================================

    /**
     * Require {@link AConstraint} on the annotation type.
     *
     * @param annotationType annotation type
     * @return {@link AConstraint} on the annotation class
     * @throws ConstraintAnalysisException if the annotation type is not annotated with {@link AConstraint}
     */
    private static AConstraint requireAConstraint(Class<? extends Annotation> annotationType) {
        AConstraint aConstraint = annotationType.getAnnotation(AConstraint.class);
        if (aConstraint != null) { return aConstraint; }
        throw new ConstraintAnalysisException("%s is not annotated with %s", annotationType, AConstraint.class);
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

    // ==================== criterion factory list ======================================

    /**
     * Get criterion factory list for the specified constraint type.
     *
     * @param constraintType constraint type
     * @param <A> type of the constraint
     * @return criterion factory list for the specified constraint type, or {@code null} if the constraint is constraint
     * used only to combine other constraints
     */
    private static <A extends Annotation> List<CriterionFactory<A, ?, ?>> getCriterionFactoryList(
            Class<A> constraintType) {
        Field field = ReflectionUtils.getDeclaredField(constraintType, "CRITERION_FACTORY_LIST");
        return (field == null) ? null : ReflectionUtils.getValue(null, field);
    }

    // ==================== value extractor list ========================================

    /**
     * Get value extractor list for the specified constraint type.
     *
     * @param constraintType constraint type
     * @return value extractor list for the specified constraint type
     */
    private static List<ValueExtractor> getValueExtractorList(Class<? extends Annotation> constraintType) {
        Field field = ReflectionUtils.getDeclaredField(constraintType, "VALUE_EXTRACTOR_LIST");
        return (field == null) ? new LinkedList<>() : ReflectionUtils.getValue(null, field);
    }

}
