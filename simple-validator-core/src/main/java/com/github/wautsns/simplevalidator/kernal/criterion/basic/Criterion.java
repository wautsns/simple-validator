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

import com.github.wautsns.simplevalidator.kernal.criterion.util.CriterionUtils;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;

import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Criterion.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class Criterion {

    /** Validation failure enhancers. */
    private List<UnaryOperator<ValidationFailure>> validationFailureEnhancers;

    /**
     * Add validation failure enhancer.
     *
     * <p>If the validationFailureEnhancer is {@code null}, or the criterion is the truth, it will not be added.
     *
     * @param validationFailureEnhancer validation failure enhancer
     */
    public final void addValidationFailureEnhancer(UnaryOperator<ValidationFailure> validationFailureEnhancer) {
        if (validationFailureEnhancer == null || CriterionUtils.isTheTruth(this)) { return; }
        if (validationFailureEnhancers == null) { validationFailureEnhancers = new LinkedList<>(); }
        validationFailureEnhancers.add(validationFailureEnhancer);
    }

    /**
     * Enhance validation failure.
     *
     * @param validationFailure validation failure
     * @return validation failure after enhancing
     */
    protected final ValidationFailure enhanceValidationFailure(ValidationFailure validationFailure) {
        if (validationFailureEnhancers == null) { return validationFailure; }
        for (UnaryOperator<ValidationFailure> validationFailureEnhancer : validationFailureEnhancers) {
            validationFailure = validationFailureEnhancer.apply(validationFailure);
        }
        return validationFailure;
    }

}
