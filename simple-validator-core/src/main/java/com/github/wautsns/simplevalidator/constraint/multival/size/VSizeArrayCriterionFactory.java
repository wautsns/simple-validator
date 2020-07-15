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

import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.factory.special.ArrayTypeCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VSizeArrayCriterionFactory extends ArrayTypeCriterionFactory<VSize> {

    /** {@code VSizeArrayCriterionFactory} instance. */
    public static final VSizeArrayCriterionFactory INSTANCE = new VSizeArrayCriterionFactory();

    @Override
    protected <T> void processTArray(ConstrainedNode node, VSize constraint, TCriteria<T[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(value -> {
            if (value.length >= min && value.length <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processIntArray(ConstrainedNode node, VSize constraint, TCriteria<int[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(value -> {
            if (value.length >= min && value.length <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processLongArray(ConstrainedNode node, VSize constraint, TCriteria<long[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(value -> {
            if (value.length >= min && value.length <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processBooleanArray(ConstrainedNode node, VSize constraint, TCriteria<boolean[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(value -> {
            if (value.length >= min && value.length <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processCharArray(ConstrainedNode node, VSize constraint, TCriteria<char[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(value -> {
            if (value.length >= min && value.length <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processByteArray(ConstrainedNode node, VSize constraint, TCriteria<byte[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(value -> {
            if (value.length >= min && value.length <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processDoubleArray(ConstrainedNode node, VSize constraint, TCriteria<double[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(value -> {
            if (value.length >= min && value.length <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processShortArray(ConstrainedNode node, VSize constraint, TCriteria<short[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(value -> {
            if (value.length >= min && value.length <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        });
    }

    @Override
    protected void processFloatArray(ConstrainedNode node, VSize constraint, TCriteria<float[]> wip) {
        int min = constraint.min();
        int max = constraint.max();
        wip.add(value -> {
            if (value.length >= min && value.length <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        });
    }

}
