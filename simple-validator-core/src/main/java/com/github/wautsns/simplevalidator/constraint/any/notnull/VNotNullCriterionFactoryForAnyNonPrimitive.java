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
package com.github.wautsns.simplevalidator.constraint.any.notnull;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForAnyNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * VNotNull criterion factory for any non-primitive value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VNotNullCriterionFactoryForAnyNonPrimitive extends CriterionFactoryForAnyNonPrimitive<VNotNull> {

    /** {@code VNotNullCriterionFactoryForAnyNonPrimitive} instance. */
    public static final VNotNullCriterionFactoryForAnyNonPrimitive INSTANCE = new VNotNullCriterionFactoryForAnyNonPrimitive();

    @Override
    public void process(ConstrainedNode node, VNotNull constraint, CriteriaForNonPrimitive<Object> wip) {
        wip.add(CRITERION);
    }

    // #################### criterion ###################################################

    /** Criterion for not null. */
    protected static final CriterionForNonPrimitive<Object> CRITERION = new CriterionForNonPrimitive<Object>() {
        @Override
        protected ValidationFailure testWithoutEnhancingFailure(Object value) {
            return (value != null) ? null : new ValidationFailure(null);
        }
    };

}
