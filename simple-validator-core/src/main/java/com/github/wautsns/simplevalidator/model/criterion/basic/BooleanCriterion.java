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
package com.github.wautsns.simplevalidator.model.criterion.basic;

import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;

import java.util.function.UnaryOperator;

/**
 * Criterion for {@code boolean} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public interface BooleanCriterion extends PrimitiveCriterion<Boolean> {

    /**
     * Test value.
     *
     * @param value value
     * @return validation failure, or {@code null} if the validation is passed.
     */
    ValidationFailure test(boolean value);

    @Override
    default ValidationFailure testWrappedValue(Boolean wrappedValue) {
        return test(wrappedValue);
    }

    @Override
    default BooleanCriterion enhanceFailure(UnaryOperator<ValidationFailure> enhancer) {
        return value -> {
            ValidationFailure failure = test(value);
            return (failure == null) ? null : enhancer.apply(failure);
        };
    }

    /** the truth */
    BooleanCriterion TRUTH = any -> null;

}