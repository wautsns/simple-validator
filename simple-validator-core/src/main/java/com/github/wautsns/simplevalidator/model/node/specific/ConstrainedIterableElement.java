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
 * Constrained iterable element.
 *
 * @author wautsns
 * @since Mar 13, 2020
 */
public class ConstrainedIterableElement extends ConstrainedTypeArg {

    /** name of the node */
    public static final String NAME = "[i]";

    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return CriterionWrapper.INSTANCE;
    }

    public ConstrainedIterableElement(
            ConstrainedType parent, Type type, List<Short> indexes,
            Map<List<Short>, List<Annotation>> indexesConstraints) {
        super(parent, type, NAME, indexes, indexesConstraints);
    }

    public static class Factory implements ConstrainedTypeArg.Factory {

        @Override
        public Class<?> getTargetClass() {
            return Iterable.class;
        }

        @Override
        public short getTypeArgIndex() {
            return 0;
        }

        @Override
        public ConstrainedTypeArg produce(
                ConstrainedType parent, Type type, List<Short> indexes,
                Map<List<Short>, List<Annotation>> indexesConstraints) {
            return new ConstrainedIterableElement(parent, type, indexes, indexesConstraints);
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class CriterionWrapper implements Criterion.Wrapper {

        public static final CriterionWrapper INSTANCE = new CriterionWrapper();

        @Override
        public <T> TCriterion<Iterable<T>> wrapTCriterion(TCriterion<T> criterion) {
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

        @Override
        public TCriterion<Iterable<Boolean>> wrapBooleanCriterion(BooleanCriterion criterion) {
            return iterable -> {
                int index = 0;
                for (Boolean value : iterable) {
                    ValidationFailure failure = criterion.testWrappedValue(value);
                    if (failure != null) { return failure.addIndicator(index); }
                    index++;
                }
                return null;
            };
        }

        @Override
        public TCriterion<Iterable<Character>> wrapCharCriterion(CharCriterion criterion) {
            return iterable -> {
                int index = 0;
                for (Character value : iterable) {
                    ValidationFailure failure = criterion.testWrappedValue(value);
                    if (failure != null) { return failure.addIndicator(index); }
                    index++;
                }
                return null;
            };
        }

        @Override
        public TCriterion<Iterable<Byte>> wrapByteCriterion(ByteCriterion criterion) {
            return iterable -> {
                int index = 0;
                for (Byte value : iterable) {
                    ValidationFailure failure = criterion.testWrappedValue(value);
                    if (failure != null) { return failure.addIndicator(index); }
                    index++;
                }
                return null;
            };
        }

        @Override
        public TCriterion<Iterable<Short>> wrapShortCriterion(ShortCriterion criterion) {
            return iterable -> {
                int index = 0;
                for (Short value : iterable) {
                    ValidationFailure failure = criterion.testWrappedValue(value);
                    if (failure != null) { return failure.addIndicator(index); }
                    index++;
                }
                return null;
            };
        }

        @Override
        public TCriterion<Iterable<Integer>> wrapIntCriterion(IntCriterion criterion) {
            return iterable -> {
                int index = 0;
                for (Integer value : iterable) {
                    ValidationFailure failure = criterion.testWrappedValue(value);
                    if (failure != null) { return failure.addIndicator(index); }
                    index++;
                }
                return null;
            };
        }

        @Override
        public TCriterion<Iterable<Long>> wrapLongCriterion(LongCriterion criterion) {
            return iterable -> {
                int index = 0;
                for (Long value : iterable) {
                    ValidationFailure failure = criterion.testWrappedValue(value);
                    if (failure != null) { return failure.addIndicator(index); }
                    index++;
                }
                return null;
            };
        }

        @Override
        public TCriterion<Iterable<Float>> wrapFloatCriterion(FloatCriterion criterion) {
            return iterable -> {
                int index = 0;
                for (Float value : iterable) {
                    ValidationFailure failure = criterion.testWrappedValue(value);
                    if (failure != null) { return failure.addIndicator(index); }
                    index++;
                }
                return null;
            };
        }

        @Override
        public TCriterion<Iterable<Double>> wrapDoubleCriterion(DoubleCriterion criterion) {
            return iterable -> {
                int index = 0;
                for (Double value : iterable) {
                    ValidationFailure failure = criterion.testWrappedValue(value);
                    if (failure != null) { return failure.addIndicator(index); }
                    index++;
                }
                return null;
            };
        }

    }

}