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
package com.github.wautsns.simplevalidator.kernal.criterion.basic;

import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;

/**
 * Criterion for {@code long} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class CriterionForLong extends CriterionForPrimitive<Long> {

    /** Truth. */
    public static final CriterionForLong TRUTH = new CriterionForLong() {
        @Override
        protected ValidationFailure testWithoutEnhancingFailure(long value) {
            return null;
        }
    };

    /**
     * Test {@code long} value.
     *
     * @param value {@code long} value
     * @return validation failure, or {@code null} if the validation is passed.
     */
    public final ValidationFailure test(long value) {
        ValidationFailure validationFailure = testWithoutEnhancingFailure(value);
        return (validationFailure == null) ? null : enhanceValidationFailure(validationFailure);
    }

    @Override
    public ValidationFailure testWrappedPrimitiveValue(Long value) {
        return test(value);
    }

    /**
     * Test {@code long} value without enhancing failure.
     *
     * @param value {@code long} value
     * @return validation failure without enhancing failure, or {@code null} if the validation is passed.
     */
    protected abstract ValidationFailure testWithoutEnhancingFailure(long value);

}
