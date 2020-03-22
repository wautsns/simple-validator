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
package com.github.wautsns.simplevalidator.model.node.extraction.type;

import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.model.node.ConstrainedTypeContainer;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.Getter;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;

/**
 * Constrained extracted type.
 *
 * @author wautsns
 * @since Mar 18, 2020
 */
@Getter
public class ConstrainedExtractedType extends ConstrainedTypeContainer {

    /** type container */
    private final ConstrainedTypeContainer typeContainer;
    /** criterion wrapper */
    private final Criterion.Wrapper criterionWrapper;

    /**
     * The method is equal to {@link #getTypeContainer()}.
     *
     * @return type container
     * @see #getTypeContainer()
     */
    @Override
    public ConstrainedNode getParent() {
        return getTypeContainer();
    }

    // -------------------- constructor -------------------------------------------------

    /**
     * Construct a constrained extracted type.
     *
     * @param typeContainer type container
     * @param metadata metadata
     * @param annotatedType annotated type
     */
    public ConstrainedExtractedType(
            ConstrainedTypeContainer typeContainer, Metadata metadata, AnnotatedType annotatedType) {
        super(typeContainer, metadata.getName(), annotatedType);
        this.typeContainer = typeContainer;
        this.criterionWrapper = metadata.getCriterionWrapper();
    }

    // -------------------- metadata ----------------------------------------------------

    /** constrained extracted type metadata */
    public abstract static class Metadata {

        /** class: AnnotatedTypeFactory$AnnotatedTypeBaseImpl */
        private static final Class<?> ANNOTATED_TYPE_BASE_IMPL = ReflectionUtils.requireClass(
                "sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl");

        /**
         * Extracted annotated type from the specific annotated type.
         *
         * @param target annotated type
         * @return extracted annotated type, or {@code null} if not supported
         */
        public final AnnotatedType extract(AnnotatedType target) {
            if (ANNOTATED_TYPE_BASE_IMPL == target.getClass()) {
                return extractFromTypeBase(target);
            } else if (target instanceof AnnotatedParameterizedType) {
                return extractFromParameterizedType((AnnotatedParameterizedType) target);
            } else if (target instanceof AnnotatedArrayType) {
                return extractFromArrayType((AnnotatedArrayType) target);
            } else if (target instanceof AnnotatedTypeVariable) {
                return extractFromTypeVariable((AnnotatedTypeVariable) target);
            } else if (target instanceof AnnotatedWildcardType) {
                return extractFromWildcardType((AnnotatedWildcardType) target);
            } else {
                return null;
            }
        }

        /**
         * Extracted annotated type from the annotated type base type(eg. List&lt;String&gt;...).
         *
         * @param annotatedType annotated type
         * @return extracted annotated type, or {@code null} if not supported
         */
        private AnnotatedType extractFromTypeBase(AnnotatedType annotatedType) {
            return null;
        }

        /**
         * Extracted annotated type from the annotated parameterized type(eg. List&lt;String&gt;...).
         *
         * @param annotatedType annotated type
         * @return extracted annotated type, or {@code null} if not supported
         */
        private AnnotatedType extractFromParameterizedType(AnnotatedParameterizedType annotatedType) {
            Class<?> clazz = TypeUtils.getClass(annotatedType.getType());
            TypeParameterMetadata typeParameterMetadata = getTypeParameterMetadata();
            if (typeParameterMetadata == null) { return null; }
            Class<?> typeContainer = typeParameterMetadata.getTypeContainer();
            int typeParameterIndex = typeParameterMetadata.getTypeParameterIndex();
            typeParameterIndex = TypeUtils.getTypeParameterIndex(clazz, typeContainer, typeParameterIndex);
            if (typeParameterIndex < 0) { return null; }
            return annotatedType.getAnnotatedActualTypeArguments()[typeParameterIndex];
        }

        /**
         * Get type parameter metadata.
         *
         * @return type parameter metadata, or {@code null} if not supported
         */
        protected TypeParameterMetadata getTypeParameterMetadata() {
            return null;
        }

        /** type parameter metadata */
        protected interface TypeParameterMetadata {

            /**
             * Get type container.
             *
             * @return type container
             */
            Class<?> getTypeContainer();

            /**
             * Get type parameter index.
             *
             * @return type parameter index
             */
            int getTypeParameterIndex();

        }

        /**
         * Extracted annotated type from the annotated array type(eg. String[], int[]...).
         *
         * @param annotatedType annotated type
         * @return extracted annotated type, or {@code null} if not supported
         */
        protected AnnotatedType extractFromArrayType(AnnotatedArrayType annotatedType) {
            return null;
        }

        /**
         * Extracted annotated type from the annotated type variable(eg. &lt;T&gt;...).
         *
         * @param annotatedType annotated type
         * @return extracted annotated type, or {@code null} if not supported
         */
        private AnnotatedType extractFromTypeVariable(AnnotatedTypeVariable annotatedType) {
            for (AnnotatedType annotatedBound : annotatedType.getAnnotatedBounds()) {
                AnnotatedType extractedType = extract(annotatedBound);
                if (extractedType != null) { return extractedType; }
            }
            return null;
        }

        /**
         * Extracted annotated type from the annotated wildcard variable(eg. &lt;? extends Number&gt;...).
         *
         * @param annotatedType annotated type
         * @return extracted annotated type, or {@code null} if not supported
         */
        private AnnotatedType extractFromWildcardType(AnnotatedWildcardType annotatedType) {
            for (AnnotatedType annotatedLowerBound : annotatedType.getAnnotatedLowerBounds()) {
                AnnotatedType extractedType = extract(annotatedLowerBound);
                if (extractedType != null) { return extractedType; }
            }
            for (AnnotatedType annotatedUpperBound : annotatedType.getAnnotatedUpperBounds()) {
                AnnotatedType extractedType = extract(annotatedUpperBound);
                if (extractedType != null) { return extractedType; }
            }
            return null;
        }

        /**
         * Get name of the extracted type.
         *
         * @return name of the extracted type
         */
        public abstract String getName();

        /**
         * Get criterion wrapper.
         *
         * @return criterion wrapper
         */
        public abstract Criterion.Wrapper getCriterionWrapper();

    }

}