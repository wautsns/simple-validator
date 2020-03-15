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
 * Constrained map key.
 *
 * @author wautsns
 * @since Mar 13, 2020
 */
public class ConstrainedMapKey extends ConstrainedTypeArg {

    /** name of the node */
    public static final String NAME = "@key";

    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return CriterionWrapper.INSTANCE;
    }

    public ConstrainedMapKey(
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
            return 0;
        }

        @Override
        public ConstrainedTypeArg produce(
                ConstrainedType parent, Type type, List<Short> indexes,
                Map<List<Short>, List<Annotation>> indexesConstraints) {
            return new ConstrainedMapKey(parent, type, indexes, indexesConstraints);
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class CriterionWrapper implements Criterion.Wrapper {

        public static final CriterionWrapper INSTANCE = new CriterionWrapper();

        @Override
        public <T> TCriterion<Map<T, ?>> wrapTCriterion(TCriterion<T> criterion) {
            return map -> {
                for (Map.Entry<T, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getKey());
                    if (failure != null) { return failure;}
                }
                return null;
            };
        }

        @Override
        public TCriterion<Map<Boolean, ?>> wrapBooleanCriterion(BooleanCriterion criterion) {
            return map -> {
                for (Map.Entry<Boolean, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getKey());
                    if (failure != null) { return failure;}
                }
                return null;
            };
        }

        @Override
        public TCriterion<Map<Character, ?>> wrapCharCriterion(CharCriterion criterion) {
            return map -> {
                for (Map.Entry<Character, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getKey());
                    if (failure != null) { return failure;}
                }
                return null;
            };
        }

        @Override
        public TCriterion<Map<Byte, ?>> wrapByteCriterion(ByteCriterion criterion) {
            return map -> {
                for (Map.Entry<Byte, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getKey());
                    if (failure != null) { return failure;}
                }
                return null;
            };
        }

        @Override
        public TCriterion<Map<Short, ?>> wrapShortCriterion(ShortCriterion criterion) {
            return map -> {
                for (Map.Entry<Short, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getKey());
                    if (failure != null) { return failure;}
                }
                return null;
            };
        }

        @Override
        public TCriterion<Map<Integer, ?>> wrapIntCriterion(IntCriterion criterion) {
            return map -> {
                for (Map.Entry<Integer, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getKey());
                    if (failure != null) { return failure;}
                }
                return null;
            };
        }

        @Override
        public TCriterion<Map<Long, ?>> wrapLongCriterion(LongCriterion criterion) {
            return map -> {
                for (Map.Entry<Long, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getKey());
                    if (failure != null) { return failure;}
                }
                return null;
            };
        }

        @Override
        public TCriterion<Map<Float, ?>> wrapFloatCriterion(FloatCriterion criterion) {
            return map -> {
                for (Map.Entry<Float, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getKey());
                    if (failure != null) { return failure;}
                }
                return null;
            };
        }

        @Override
        public TCriterion<Map<Double, ?>> wrapDoubleCriterion(DoubleCriterion criterion) {
            return map -> {
                for (Map.Entry<Double, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getKey());
                    if (failure != null) { return failure;}
                }
                return null;
            };
        }

    }

}
