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

import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.special.ComparableNumberCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VDomainComparableNumberCriterionFactory extends ComparableNumberCriterionFactory<VDomain> {

    public static final VDomainComparableNumberCriterionFactory INSTANCE = new VDomainComparableNumberCriterionFactory();

    @Override
    public void process(ConstrainedNode node, VDomain constraint, TCriteria<Comparable<Number>> wip) {
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
    protected static TCriterion<Comparable<Number>> produce(
            ConstrainedNode node, VDomain constraint) {
        Predicate predicate = DomainUtils.init(constraint.value())
                .forComparableNumber((Class) TypeUtils.getClass(node.getType()));
        if (predicate == null) { return null; }
        return value -> predicate.test(value) ? null : new ValidationFailure(value);
    }

}
