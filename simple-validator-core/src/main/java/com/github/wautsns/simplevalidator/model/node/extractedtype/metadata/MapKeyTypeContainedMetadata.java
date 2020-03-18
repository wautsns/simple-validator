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
package com.github.wautsns.simplevalidator.model.node.extractedtype.metadata;

import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.extractedtype.ConstrainedExtractedType;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.Map;

/**
 * Type contained metadata of map key.
 *
 * @author wautsns
 * @since Mar 18, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapKeyTypeContainedMetadata extends ConstrainedExtractedType.Metadata {

    public static final MapKeyTypeContainedMetadata INSTANCE = new MapKeyTypeContainedMetadata();

    @Override
    protected AnnotatedType extractFromParameterizedType(AnnotatedParameterizedType annotatedType) {
        if (!TypeUtils.isAssignableTo(annotatedType.getType(), Map.class)) { return null; }
        return annotatedType.getAnnotatedActualTypeArguments()[0];
    }

    @Override
    public String getName() {
        return "@MAP_KEY";
    }

    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return CriterionWrapper.INSTANCE;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class CriterionWrapper extends Criterion.Wrapper {

        public static final CriterionWrapper INSTANCE = new CriterionWrapper();

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

    }

}
