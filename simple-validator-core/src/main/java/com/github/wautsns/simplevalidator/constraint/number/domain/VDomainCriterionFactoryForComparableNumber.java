/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.wautsns.simplevalidator.constraint.number.domain;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForComparableNumber;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

/**
 * VDomain criterion factory for {@code Comparable & Number} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VDomainCriterionFactoryForComparableNumber extends CriterionFactoryForComparableNumber<VDomain> {

    /** {@code VDomainCriterionFactoryForComparableNumber} instance. */
    public static final VDomainCriterionFactoryForComparableNumber INSTANCE = new VDomainCriterionFactoryForComparableNumber();

    @Override
    public void process(ConstrainedNode node, VDomain constraint, CriteriaForNonPrimitive<Comparable<Number>> wip) {
        wip.add(produce(node, constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param node constrained node
     * @param constraint constraint
     * @return criterion
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected static CriterionForNonPrimitive<Comparable<Number>> produce(
            ConstrainedNode node, VDomain constraint) {
        Predicate predicate = DomainUtils.init(constraint.value())
                .initPredicateForComparableNumber((Class) TypeUtils.getClass(node.getType()));
        if (predicate == null) { return null; }
        return new CriterionForNonPrimitive<Comparable<Number>>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Comparable<Number> value) {
                return predicate.test(value) ? null : new ValidationFailure(value);
            }
        };
    }

}
