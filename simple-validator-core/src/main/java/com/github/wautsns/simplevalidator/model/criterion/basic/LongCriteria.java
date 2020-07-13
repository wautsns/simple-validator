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

/**
 * Criteria for {@code long} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class LongCriteria extends Criteria<LongCriterion> implements LongCriterion {

    @Override
    public ValidationFailure test(long value) {
        for (LongCriterion criterion : criterionList) {
            ValidationFailure failure = criterion.test(value);
            if (failure != null) { return failure; }
        }
        return null;
    }

}
