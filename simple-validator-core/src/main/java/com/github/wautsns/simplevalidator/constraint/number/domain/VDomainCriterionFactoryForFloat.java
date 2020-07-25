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
package com.github.wautsns.simplevalidator.constraint.number.domain;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForFloat;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForFloat;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactoryForFloat;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.function.FloatPredicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * VDomain criterion factory for {@code float} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VDomainCriterionFactoryForFloat extends CriterionFactoryForFloat<VDomain> {

    /** {@code VDomainCriterionFactoryForFloat} instance. */
    public static final VDomainCriterionFactoryForFloat INSTANCE = new VDomainCriterionFactoryForFloat();

    @Override
    public void process(ConstrainedNode node, VDomain constraint, CriteriaForFloat wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @return criterion
     */
    protected static CriterionForFloat produce(VDomain constraint) {
        FloatPredicate predicate = DomainUtils.init(constraint.value()).initPredicateForFloat();
        if (predicate == null) { return null; }
        return new CriterionForFloat() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(float value) {
                return predicate.test(value) ? null : new ValidationFailure(value);
            }
        };
    }

}
