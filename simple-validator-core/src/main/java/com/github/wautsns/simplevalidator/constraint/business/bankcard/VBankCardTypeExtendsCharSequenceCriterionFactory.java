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
package com.github.wautsns.simplevalidator.constraint.business.bankcard;

import com.github.wautsns.simplevalidator.model.criterion.factory.TCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriterion;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.normal.TypeUtils;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VBankCardTypeExtendsCharSequenceCriterionFactory implements TCriterionFactory<VBankCard, CharSequence> {

    @Override
    public boolean appliesTo(ConstrainedNode node, VBankCard constraint) {
        return TypeUtils.isAssignableTo(node.getType(), CharSequence.class);
    }

    @Override
    public void process(ConstrainedNode node, VBankCard constraint, TCriteria<CharSequence> wip) {
        wip.add(produceWithLuhn());
    }

    // ------------------------- criterion -----------------------------------------

    protected static TCriterion<CharSequence> produceWithLuhn() {
        return id -> {
            int length = id.length();
            if (length < 8 || length > 19) { return new ValidationFailure(id); }
            int sum = 0;
            for (int i = 0; i < length; i++) {
                int digit = id.charAt(i) - '0';
                if ((i & 1) == 0) {
                    sum += digit;
                } else {
                    digit <<= 1;
                    sum += (digit < 9) ? digit : (digit - 9);
                }
            }
            return ((sum % 10) == 0) ? null : new ValidationFailure(id);
        };
    }

}
