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
 * Criterion for {@code short} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class CriterionForShort extends CriterionForPrimitive<Short> {

    /** Truth. */
    public static final CriterionForShort TRUTH = new CriterionForShort() {
        @Override
        protected ValidationFailure testWithoutEnhancingFailure(short value) {
            return null;
        }
    };

    /**
     * Test {@code short} value.
     *
     * @param value {@code short} value
     * @return validation failure, or {@code null} if the validation is passed.
     */
    public final ValidationFailure test(short value) {
        ValidationFailure validationFailure = testWithoutEnhancingFailure(value);
        return (validationFailure == null) ? null : enhanceValidationFailure(validationFailure);
    }

    @Override
    public ValidationFailure testWrappedPrimitiveValue(Short value) {
        return test(value);
    }

    /**
     * Test {@code short} value without enhancing failure.
     *
     * @param value {@code short} value
     * @return validation failure without enhancing failure, or {@code null} if the validation is passed.
     */
    protected abstract ValidationFailure testWithoutEnhancingFailure(short value);

}
