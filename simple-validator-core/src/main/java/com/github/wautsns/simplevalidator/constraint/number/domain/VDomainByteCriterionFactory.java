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

import com.github.wautsns.simplevalidator.model.criterion.basic.ByteCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.ByteCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.primitive.ByteCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.function.BytePredicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VDomainByteCriterionFactory extends ByteCriterionFactory<VDomain> {

    public static final VDomainByteCriterionFactory INSTANCE = new VDomainByteCriterionFactory();

    @Override
    public void process(ConstrainedNode node, VDomain constraint, ByteCriteria wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    protected static ByteCriterion produce(VDomain constraint) {
        BytePredicate predicate = DomainUtils.init(constraint.value()).forByte();
        if (predicate == null) { return null; }
        return value -> predicate.test(value) ? null : new ValidationFailure(value);
    }

}
