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
package com.github.wautsns.simplevalidator.constraint.business.id;

import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.special.AbstractComparableNumberCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VIdIntegerLongBigIntegerCriterionFactory extends AbstractComparableNumberCriterionFactory<VId> {

    @Override
    public boolean appliesTo(ConstrainedNode node, VId constraint) {
        Type type = node.getType();
        return TypeUtils.isAssignableToAny(type, Long.class, BigInteger.class)
                || (!constraint.unsigned() && TypeUtils.isAssignableTo(type, Integer.class));
    }

    @Override
    public void process(ConstrainedNode node, VId constraint, TCriteria<Comparable<Number>> wip) {
        if (constraint.unsigned()) { wip.add(produceUnsigned(node)); }
    }

    // -------------------- criterion ---------------------------------------------------

    protected static TCriterion<Comparable<Number>> produceUnsigned(ConstrainedNode node) {
        Number max = getUnsignedMaxValue(TypeUtils.getClass(node.getType()));
        return value -> le(value, max) ? null : new ValidationFailure(value);
    }

    // ------------------------- utils ---------------------------------------------

    private static final Map<Class<?>, Number> UNSIGNED_MAX_VALUES;

    static {
        UNSIGNED_MAX_VALUES = new HashMap<>(4);
        UNSIGNED_MAX_VALUES.put(Long.class, 4294967295L);
        UNSIGNED_MAX_VALUES.put(BigInteger.class, new BigInteger("18446744073709551615"));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number & Comparable<T>> T getUnsignedMaxValue(Class<?> clazz) {
        T unsignedMaxValue = (T) UNSIGNED_MAX_VALUES.get(clazz);
        if (unsignedMaxValue != null) {
            return unsignedMaxValue;
        } else {
            throw new IllegalStateException();
        }
    }

}
