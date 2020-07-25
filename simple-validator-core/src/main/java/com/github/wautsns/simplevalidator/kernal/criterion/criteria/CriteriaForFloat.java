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
package com.github.wautsns.simplevalidator.kernal.criterion.criteria;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForFloat;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;

import java.util.LinkedList;
import java.util.List;

/**
 * Criteria for {@code float} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class CriteriaForFloat extends CriterionForFloat implements Criteria<CriterionForFloat> {

    /** Whether the criteria support {@code null} value. */
    private boolean nullable;
    /** Original criteria. */
    private final List<CriterionForFloat> originalCriteria = new LinkedList<>();

    @Override
    public void nullable() {
        nullable = true;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public List<CriterionForFloat> getOriginalCriteria() {
        return originalCriteria;
    }

    @Override
    protected ValidationFailure testWithoutEnhancingFailure(float value) {
        for (CriterionForFloat criterion : originalCriteria) {
            ValidationFailure failure = criterion.test(value);
            if (failure != null) { return failure; }
        }
        return null;
    }

}
