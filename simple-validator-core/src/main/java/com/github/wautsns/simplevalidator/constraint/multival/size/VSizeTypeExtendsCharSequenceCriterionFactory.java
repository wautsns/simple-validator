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

import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.TCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VSizeTypeExtendsCharSequenceCriterionFactory implements TCriterionFactory<VSize, CharSequence> {

    @Override
    public boolean appliesTo(ConstrainedNode node, VSize constraint) {
        return TypeUtils.isAssignableTo(node.getType(), CharSequence.class);
    }

    @Override
    public void process(ConstrainedNode node, VSize constraint, TCriteria<CharSequence> wip) {
        wip.add(produce(constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected TCriterion<CharSequence> produce(VSize constraint) {
        int min = constraint.min();
        int max = constraint.max();
        return value -> {
            if (value.length() >= min && value.length() <= max) {
                return null;
            } else {
                return new ValidationFailure(value);
            }
        };
    }

}
