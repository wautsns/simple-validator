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
package com.github.wautsns.simplevalidator.model.node.extractedtype;

import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.model.node.ConstrainedTypeContainer;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
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

    private final ConstrainedTypeContainer typeContainer;
    private final Criterion.Wrapper criterionWrapper;

    @Override
    public ConstrainedNode getParent() {
        return getTypeContainer();
    }

    // -------------------- constructor -------------------------------------------------

    public ConstrainedExtractedType(
            ConstrainedTypeContainer typeContainer, Metadata metadata, AnnotatedType annotatedType) {
        super(typeContainer, metadata.getName(), annotatedType);
        this.typeContainer = typeContainer;
        this.criterionWrapper = metadata.getCriterionWrapper();
    }

    // -------------------- metadata ----------------------------------------------------

    public abstract static class Metadata {

        private static final Class<?> ANNOTATED_TYPE_BASE_IMPL = ReflectionUtils.requireClass(
                "sun.reflect.annotation.AnnotatedTypeFactory.AnnotatedTypeBaseImpl");

        public final AnnotatedType extract(AnnotatedType annotatedType) {
            if (ANNOTATED_TYPE_BASE_IMPL.isInstance(annotatedType)) {
                return extractFromTypeBase(annotatedType);
            } else if (annotatedType instanceof AnnotatedParameterizedType) {
                return extractFromParameterizedType((AnnotatedParameterizedType) annotatedType);
            } else if (annotatedType instanceof AnnotatedArrayType) {
                return extractFromArrayType((AnnotatedArrayType) annotatedType);
            } else if (annotatedType instanceof AnnotatedTypeVariable) {
                return extractFromTypeVariable((AnnotatedTypeVariable) annotatedType);
            } else if (annotatedType instanceof AnnotatedWildcardType) {
                return extractFromWildcardType((AnnotatedWildcardType) annotatedType);
            } else {
                throw new IllegalStateException();
            }
        }

        protected AnnotatedType extractFromTypeBase(AnnotatedType annotatedType) {
            return null;
        }

        protected AnnotatedType extractFromParameterizedType(AnnotatedParameterizedType annotatedType) {
            return null;
        }

        protected AnnotatedType extractFromArrayType(AnnotatedArrayType annotatedType) {
            return null;
        }

        protected AnnotatedType extractFromTypeVariable(AnnotatedTypeVariable annotatedType) {
            AnnotatedType[] annotatedBounds = annotatedType.getAnnotatedBounds();
            return null;
        }

        protected AnnotatedType extractFromWildcardType(AnnotatedWildcardType annotatedType) {
            return null;
        }

        public abstract String getName();

        public abstract Criterion.Wrapper getCriterionWrapper();

    }

}