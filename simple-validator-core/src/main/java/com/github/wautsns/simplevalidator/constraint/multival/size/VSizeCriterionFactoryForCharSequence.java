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

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactoryForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

/**
 * VSize criterion factory for {@code CharSequence} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VSizeCriterionFactoryForCharSequence extends CriterionFactoryForNonPrimitive<VSize, CharSequence> {

    /** {@code VSizeCriterionFactoryForCharSequence} instance. */
    public static final VSizeCriterionFactoryForCharSequence INSTANCE = new VSizeCriterionFactoryForCharSequence();

    @Override
    public boolean applyTo(Type type, VSize constraint) {
        return TypeUtils.isAssignableTo(type, CharSequence.class);
    }

    @Override
    public void process(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<CharSequence> wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @return criterion
     */
    protected CriterionForNonPrimitive<CharSequence> produce(VSize constraint) {
        int min = constraint.min();
        int max = constraint.max();
        return new CriterionForNonPrimitive<CharSequence>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(CharSequence value) {
                int length = value.length();
                return (length >= min && length <= max) ? null : new ValidationFailure(value);
            }
        };
    }

}
