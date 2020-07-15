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
package com.github.wautsns.simplevalidator.model.node.extraction.type.metadata;

import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.extraction.type.ConstrainedExtractedType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The extracted iterable element type metadata.
 *
 * @author wautsns
 * @since Mar 18, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtractedIterableElementTypeMetadata extends ConstrainedExtractedType.Metadata {

    /** {@code ExtractedIterableElementTypeMetadata} instance. */
    public static final ExtractedIterableElementTypeMetadata INSTANCE = new ExtractedIterableElementTypeMetadata();

    @Override
    protected TypeParameterMetadata getTypeParameterMetadata() {
        return TYPE_PARAMETER_METADATA;
    }

    @Override
    public String getName() {
        return "@ITERABLE_ELEMENT";
    }

    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return CRITERION_WRAPPER;
    }

    // #################### internal utils ##############################################

    /** Type parameter metadata. */
    private static final TypeParameterMetadata TYPE_PARAMETER_METADATA = new TypeParameterMetadata() {

        @Override
        public Class<?> getTypeContainer() {
            return Iterable.class;
        }

        @Override
        public int getTypeParameterIndex() {
            return 0;
        }
    };

    /** Criterion wrapper. */
    private static final Criterion.Wrapper CRITERION_WRAPPER = new Criterion.Wrapper() {

        @Override
        protected <T> TCriterion<Iterable<T>> wrap(TCriterion<T> criterion) {
            return iterable -> {
                int index = 0;
                for (T value : iterable) {
                    ValidationFailure failure = criterion.test(value);
                    if (failure != null) { return failure.addIndicator(index); }
                    index++;
                }
                return null;
            };
        }

    };

}
