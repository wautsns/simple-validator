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
package com.github.wautsns.simplevalidator.constraint.algorithm.luhn;

import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.TCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

/**
 * {@code CharSequence} criterion factory for the algorithm: luhn.
 *
 * @author wautsns
 * @since Mar 19, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VLuhnCharSequenceCriterionFactory implements TCriterionFactory<VLuhn, CharSequence> {

    /** {@code VLuhnCharSequenceCriterionFactory} instance */
    public static final VLuhnCharSequenceCriterionFactory INSTANCE = new VLuhnCharSequenceCriterionFactory();

    @Override
    public boolean appliesTo(Type type, VLuhn constraint) {
        return TypeUtils.isAssignableTo(type, CharSequence.class);
    }

    @Override
    public void process(ConstrainedNode node, VLuhn constraint, TCriteria<CharSequence> wip) {
        wip.add(CRITERION);
    }

    // #################### criterion ###################################################

    /** Criterion for luhn. */
    protected static final TCriterion<CharSequence> CRITERION = id -> {
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
