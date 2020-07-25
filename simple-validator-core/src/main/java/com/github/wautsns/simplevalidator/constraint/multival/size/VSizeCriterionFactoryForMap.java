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
import java.util.Map;

/**
 * VSize criterion factory for {@code Map} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VSizeCriterionFactoryForMap extends CriterionFactoryForNonPrimitive<VSize, Map<?, ?>> {

    /** {@code VSizeCriterionFactoryForMap} instance. */
    public static final VSizeCriterionFactoryForMap INSTANCE = new VSizeCriterionFactoryForMap();

    @Override
    public boolean applyTo(Type type, VSize constraint) {
        return TypeUtils.isAssignableTo(type, Map.class);
    }

    @Override
    public void process(ConstrainedNode node, VSize constraint, CriteriaForNonPrimitive<Map<?, ?>> wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @return criterion
     */
    protected CriterionForNonPrimitive<Map<?, ?>> produce(VSize constraint) {
        int min = constraint.min();
        int max = constraint.max();
        return new CriterionForNonPrimitive<Map<?, ?>>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Map<?, ?> value) {
                int size = value.size();
                return (size >= min && size <= max) ? null : new ValidationFailure(value);
            }
        };
    }

}
