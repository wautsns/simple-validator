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

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForCharSequence;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

/**
 * VLuhn criterion factory for {@code CharSequence} value.
 *
 * @author wautsns
 * @since Mar 19, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VLuhnCriterionFactoryForCharSequence extends CriterionFactoryForCharSequence<VLuhn> {

    /** {@code VLuhnCriterionFactoryForCharSequence} instance. */
    public static final VLuhnCriterionFactoryForCharSequence INSTANCE = new VLuhnCriterionFactoryForCharSequence();

    @Override
    public boolean applyTo(Type type, VLuhn constraint) {
        return TypeUtils.isAssignableTo(type, CharSequence.class);
    }

    @Override
    public void process(ConstrainedNode node, VLuhn constraint, CriteriaForNonPrimitive<CharSequence> wip) {
        wip.add(CRITERION);
    }

    // #################### criterion ###################################################

    /** Criterion for luhn. */
    protected static final CriterionForNonPrimitive<CharSequence> CRITERION = new CriterionForNonPrimitive<CharSequence>() {
        @Override
        protected ValidationFailure testWithoutEnhancingFailure(CharSequence value) {
            int length = value.length();
            if (length < 8 || length > 19) { return new ValidationFailure(value); }
            int sum = 0;
            for (int i = 0; i < length; i++) {
                int digit = value.charAt(i) - '0';
                if ((i & 1) == 0) {
                    sum += digit;
                } else {
                    digit <<= 1;
                    sum += (digit < 9) ? digit : (digit - 9);
                }
            }
            return ((sum % 10) == 0) ? null : new ValidationFailure(value);
        }
    };

}
