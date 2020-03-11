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
package com.github.wautsns.simplevalidator.constraint.multival.size;

import com.github.wautsns.simplevalidator.model.criterion.factory.TCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriterion;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.normal.TypeUtils;

import java.util.Collection;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VSizeTypeExtendsCollectionCriterionFactory implements TCriterionFactory<VSize, Collection<?>> {

    @Override
    public boolean appliesTo(ConstrainedNode node, VSize constraint) {
        return TypeUtils.isAssignableTo(node.getType(), Collection.class);
    }

    @Override
    public void process(ConstrainedNode node, VSize constraint, TCriteria<Collection<?>> wip) {
        wip.add(produce(constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected TCriterion<Collection<?>> produce(VSize constraint) {
        int min = constraint.min();
        int max = constraint.max();
        return value -> {
            if (value.size() >= min && value.size() <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        };
    }

}
