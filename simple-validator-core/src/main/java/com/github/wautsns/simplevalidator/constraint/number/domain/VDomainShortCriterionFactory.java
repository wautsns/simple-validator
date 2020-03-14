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

import com.github.wautsns.simplevalidator.model.criterion.basic.ShortCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.ShortCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.primitive.AbstractShortCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.function.ShortPredicate;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VDomainShortCriterionFactory extends AbstractShortCriterionFactory<VDomain> {

    @Override
    public void process(ConstrainedNode node, VDomain constraint, ShortCriteria wip) {
        wip.add(produce(constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected static ShortCriterion produce(VDomain constraint) {
        ShortPredicate predicate = DomainUtils.init(constraint.value()).forShort();
        if (predicate == null) { return null; }
        return value -> predicate.test(value) ? null : new ValidationFailure(value);
    }

}
