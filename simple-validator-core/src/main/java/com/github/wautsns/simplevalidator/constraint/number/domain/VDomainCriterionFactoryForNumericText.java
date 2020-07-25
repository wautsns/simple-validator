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
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForCharSequence;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

/**
 * VDomain criterion factory for numeric-text value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VDomainCriterionFactoryForNumericText extends CriterionFactoryForCharSequence<VDomain> {

    /** {@code VDomainCriterionFactoryForNumericText} instance. */
    public static final VDomainCriterionFactoryForNumericText INSTANCE = new VDomainCriterionFactoryForNumericText();

    @Override
    public void process(ConstrainedNode node, VDomain constraint, CriteriaForNonPrimitive<CharSequence> wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @return criterion
     */
    protected static CriterionForNonPrimitive<CharSequence> produce(VDomain constraint) {
        Predicate<CharSequence> predicate = DomainUtils.init(constraint.value()).initPredicateForNumericText();
        if (predicate == null) { return null; }
        return new CriterionForNonPrimitive<CharSequence>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(CharSequence value) {
                return predicate.test(value) ? null : new ValidationFailure(value);
            }
        };
    }

}
