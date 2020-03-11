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

import com.github.wautsns.simplevalidator.model.criterion.factory.TCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriterion;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.normal.TypeUtils;

import java.util.function.Predicate;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VDomainNumericTextCriterionFactory implements TCriterionFactory<VDomain, CharSequence> {

    @Override
    public boolean appliesTo(ConstrainedNode node, VDomain constraint) {
        return TypeUtils.isAssignableTo(node.getType(), CharSequence.class);
    }

    @Override
    public void process(ConstrainedNode node, VDomain constraint, TCriteria<CharSequence> wip) {
        wip.add(produce(constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected static TCriterion<CharSequence> produce(VDomain constraint) {
        Predicate<CharSequence> predicate = DomainUtils.init(constraint.value()).forNumericText();
        if (predicate == null) { return null; }
        return value -> predicate.test(value) ? null : new ValidationFailure(value);
    }

}
