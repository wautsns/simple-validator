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
package com.github.wautsns.simplevalidator.model.node.specific;

import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedType;
import com.github.wautsns.simplevalidator.model.node.ConstrainedTypeArg;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Constrained map value.
 *
 * @author wautsns
 * @since Mar 13, 2020
 */
public class ConstrainedMapValue extends ConstrainedTypeArg {

    /** name of the node */
    public static final String NAME = "@value";

    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return CriterionWrapper.INSTANCE;
    }

    public ConstrainedMapValue(
            ConstrainedType parent, Type type, List<Short> indexes,
            Map<List<Short>, List<Annotation>> indexesConstraints) {
        super(parent, type, NAME, indexes, indexesConstraints);
    }

    public static class Factory implements ConstrainedTypeArg.Factory {

        @Override
        public Class<?> getTypeClass() {
            return Map.class;
        }

        @Override
        public short getTypeArgIndex() {
            return 1;
        }

        @Override
        public ConstrainedTypeArg produce(
                ConstrainedType parent, Type type, List<Short> indexes,
                Map<List<Short>, List<Annotation>> indexesConstraints) {
            return new ConstrainedMapValue(parent, type, indexes, indexesConstraints);
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class CriterionWrapper extends Criterion.Wrapper {

        public static final CriterionWrapper INSTANCE = new CriterionWrapper();

        @Override
        protected <T> TCriterion<Map<?, T>> wrap(TCriterion<T> criterion) {
            return map -> {
                for (Map.Entry<?, T> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getValue());
                    if (failure != null) { return failure.addIndicator(entry.getKey()); }
                }
                return null;
            };
        }

    }

}
