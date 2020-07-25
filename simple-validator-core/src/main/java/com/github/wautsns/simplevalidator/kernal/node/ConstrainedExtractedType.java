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
package com.github.wautsns.simplevalidator.kernal.node;

import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapper;
import com.github.wautsns.simplevalidator.kernal.extractor.type.basic.AnnotatedTypeExtractor;
import lombok.Getter;

import java.lang.reflect.AnnotatedType;

/**
 * The constrained extracted type.
 *
 * @author wautsns
 * @since Mar 18, 2020
 */
@Getter
public class ConstrainedExtractedType extends ConstrainedTypeContainer {

    /** Type container. */
    private final ConstrainedTypeContainer typeContainer;
    /** Criterion wrapper. */
    private final CriterionWrapper criterionWrapper;

    /**
     * Get type container.
     *
     * @return type container
     */
    @Override
    public ConstrainedNode getParent() {
        return getTypeContainer();
    }

    // #################### constructor #################################################

    /**
     * Construct a constrained extracted type.
     *
     * @param typeContainer type container
     * @param extractor annotated type extractor
     * @param annotatedType annotated type
     */
    public ConstrainedExtractedType(
            ConstrainedTypeContainer typeContainer, AnnotatedTypeExtractor extractor, AnnotatedType annotatedType) {
        super(typeContainer, extractor.getNameOfExtractedType(), annotatedType);
        this.typeContainer = typeContainer;
        this.criterionWrapper = extractor.getCriterionWrapper();
    }

}
