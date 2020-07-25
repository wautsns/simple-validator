/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.wautsns.simplevalidator.constraint.multival.size;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForArray;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * VSize criterion factory for array.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VSizeCriterionFactoryForArray extends CriterionFactoryForArray<VSize> {

    /** {@code VSizeCriterionFactoryForArray} instance. */
    public static final VSizeCriterionFactoryForArray INSTANCE = new VSizeCriterionFactoryForArray();

    @Override
    protected <T> void processTArray(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<T[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(new CriterionForNonPrimitive<T[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(T[] value) {
                return (value.length >= min && value.length <= max) ? null : new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processIntArray(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<int[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(new CriterionForNonPrimitive<int[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(int[] value) {
                return (value.length >= min && value.length <= max) ? null : new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processLongArray(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<long[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(new CriterionForNonPrimitive<long[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(long[] value) {
                return (value.length >= min && value.length <= max) ? null : new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processBooleanArray(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<boolean[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(new CriterionForNonPrimitive<boolean[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(boolean[] value) {
                return (value.length >= min && value.length <= max) ? null : new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processCharArray(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<char[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(new CriterionForNonPrimitive<char[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(char[] value) {
                return (value.length >= min && value.length <= max) ? null : new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processByteArray(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<byte[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(new CriterionForNonPrimitive<byte[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(byte[] value) {
                return (value.length >= min && value.length <= max) ? null : new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processDoubleArray(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<double[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(new CriterionForNonPrimitive<double[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(double[] value) {
                return (value.length >= min && value.length <= max) ? null : new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processShortArray(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<short[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(new CriterionForNonPrimitive<short[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(short[] value) {
                return (value.length >= min && value.length <= max) ? null : new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processFloatArray(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<float[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(new CriterionForNonPrimitive<float[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(float[] value) {
                return (value.length >= min && value.length <= max) ? null : new ValidationFailure(value);
            }
        });
    }

}
