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

import com.github.wautsns.simplevalidator.model.criterion.basic.BooleanCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.ByteCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.CharCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.DoubleCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.FloatCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.IntCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.LongCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.ShortCriterion;
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
 * Constrained array component.
 *
 * @author wautsns
 * @since Mar 13, 2020
 */
public class ConstrainedArrayComponent extends ConstrainedTypeArg {

    /** name of the node */
    public static final String NAME = "[i]";

    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return CriterionWrapper.INSTANCE;
    }

    public ConstrainedArrayComponent(
            ConstrainedType parent, Type type, List<Short> indexes,
            Map<List<Short>, List<Annotation>> indexesConstraints) {
        super(parent, type, NAME, indexes, indexesConstraints);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class CriterionWrapper extends Criterion.Wrapper {

        public static final CriterionWrapper INSTANCE = new CriterionWrapper();

        @Override
        protected <T> TCriterion<T[]> wrap(TCriterion<T> criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure == null) { continue; }
                    failure.addIndicator(i);
                    return failure;
                }
                return null;
            };
        }

        @Override
        protected TCriterion<boolean[]> wrap(BooleanCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure == null) { continue; }
                    failure.addIndicator(i);
                    return failure;
                }
                return null;
            };
        }

        @Override
        protected TCriterion<char[]> wrap(CharCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure == null) { continue; }
                    failure.addIndicator(i);
                    return failure;
                }
                return null;
            };
        }

        @Override
        protected TCriterion<byte[]> wrap(ByteCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure == null) { continue; }
                    failure.addIndicator(i);
                    return failure;
                }
                return null;
            };
        }

        @Override
        protected TCriterion<short[]> wrap(ShortCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure == null) { continue; }
                    failure.addIndicator(i);
                    return failure;
                }
                return null;
            };
        }

        @Override
        protected TCriterion<int[]> wrap(IntCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure == null) { continue; }
                    failure.addIndicator(i);
                    return failure;
                }
                return null;
            };
        }

        @Override
        protected TCriterion<long[]> wrap(LongCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure == null) { continue; }
                    failure.addIndicator(i);
                    return failure;
                }
                return null;
            };
        }

        @Override
        protected TCriterion<float[]> wrap(FloatCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure == null) { continue; }
                    failure.addIndicator(i);
                    return failure;
                }
                return null;
            };
        }

        @Override
        protected TCriterion<double[]> wrap(DoubleCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure == null) { continue; }
                    failure.addIndicator(i);
                    return failure;
                }
                return null;
            };
        }

    }

}
