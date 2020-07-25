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

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForDouble;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForDouble;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactoryForDouble;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.DoublePredicate;

/**
 * VDomain criterion factory for {@code double} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VDomainCriterionFactoryForDouble extends CriterionFactoryForDouble<VDomain> {

    /** {@code VDomainCriterionFactoryForDouble} instance. */
    public static final VDomainCriterionFactoryForDouble INSTANCE = new VDomainCriterionFactoryForDouble();

    @Override
    public void process(ConstrainedNode node, VDomain constraint, CriteriaForDouble wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @return criterion
     */
    protected static CriterionForDouble produce(VDomain constraint) {
        DoublePredicate predicate = DomainUtils.init(constraint.value()).initPredicateForDouble();
        if (predicate == null) { return null; }
        return new CriterionForDouble() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(double value) {
                return predicate.test(value) ? null : new ValidationFailure(value);
            }
        };
    }

}
