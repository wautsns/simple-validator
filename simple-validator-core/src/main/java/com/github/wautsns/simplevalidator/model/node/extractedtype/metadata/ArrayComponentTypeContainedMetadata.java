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
import com.github.wautsns.simplevalidator.model.node.extractedtype.ConstrainedExtractedType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedType;

/**
 * Type contained metadata of array component.
 *
 * @author wautsns
 * @since Mar 18, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayComponentTypeContainedMetadata extends ConstrainedExtractedType.Metadata {

    public static final ArrayComponentTypeContainedMetadata INSTANCE = new ArrayComponentTypeContainedMetadata();

    @Override
    protected AnnotatedType extractFromArrayType(AnnotatedArrayType annotatedType) {
        return annotatedType.getAnnotatedGenericComponentType();
    }

    @Override
    public String getName() {
        return "@ARRAY_COMPONENT";
    }

    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return CriterionWrapper.INSTANCE;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class CriterionWrapper extends Criterion.Wrapper {

        public static final CriterionWrapper INSTANCE = new CriterionWrapper();

        @Override
        protected <T> TCriterion<T[]> wrap(TCriterion<T> criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            };
        }

        @Override
        protected TCriterion<boolean[]> wrap(BooleanCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            };
        }

        @Override
        protected TCriterion<char[]> wrap(CharCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            };
        }

        @Override
        protected TCriterion<byte[]> wrap(ByteCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            };
        }

        @Override
        protected TCriterion<short[]> wrap(ShortCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            };
        }

        @Override
        protected TCriterion<int[]> wrap(IntCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            };
        }

        @Override
        protected TCriterion<long[]> wrap(LongCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            };
        }

        @Override
        protected TCriterion<float[]> wrap(FloatCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            };
        }

        @Override
        protected TCriterion<double[]> wrap(DoubleCriterion criterion) {
            return array -> {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            };
        }

    }

}
