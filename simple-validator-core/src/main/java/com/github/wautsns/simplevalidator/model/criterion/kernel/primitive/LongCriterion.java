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
package com.github.wautsns.simplevalidator.model.criterion.kernel.primitive;

import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriterion;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.util.normal.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.function.UnaryOperator;

/**
 * Criterion for {@code long} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public interface LongCriterion extends PrimitiveCriterion<Long> {

    /**
     * Test value.
     *
     * @param value value
     * @return validation failure, or {@code null} if the validation is passed.
     */
    ValidationFailure test(long value);

    @Override
    default ValidationFailure testWrappedValue(Long wrappedValue) {
        return test(wrappedValue);
    }

    @Override
    default LongCriterion enhanceResultProcessing(UnaryOperator<ValidationFailure> adjuster) {
        return value -> adjuster.apply(test(value));
    }

    @Override
    default TCriterion<?> wrapFieldIntoType(Field field) {
        return source -> test(ReflectionUtils.getFieldLong(source, field));
    }

    @Override
    default TCriterion<long[]> wrapComponentIntoArray() {
        return array -> {
            for (int i = 0; i < array.length; i++) {
                ValidationFailure failure = test(array[i]);
                if (failure != null) { return failure.addIndicator(i); }
            }
            return null;
        };
    }

}
