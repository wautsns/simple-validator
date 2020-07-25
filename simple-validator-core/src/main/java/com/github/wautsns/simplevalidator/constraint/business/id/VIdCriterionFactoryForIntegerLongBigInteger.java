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

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForComparableNumber;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * VId criterion factory for {@code Integer}, {@code Long}, {@code BigInteger} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VIdCriterionFactoryForIntegerLongBigInteger extends CriterionFactoryForComparableNumber<VId> {

    /** {@code VIdCriterionFactoryForIntegerLongBigInteger} instance. */
    public static final VIdCriterionFactoryForIntegerLongBigInteger INSTANCE = new VIdCriterionFactoryForIntegerLongBigInteger();

    @Override
    public boolean applyTo(Type type, VId constraint) {
        return TypeUtils.isAssignableToAny(type, Long.class, BigInteger.class)
                || (!constraint.unsigned() && TypeUtils.isAssignableTo(type, Integer.class));
    }

    @Override
    public void process(ConstrainedNode node, VId constraint, CriteriaForNonPrimitive<Comparable<Number>> wip) {
        wip.add(produce(node, constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param node constrained node
     * @param constraint constraint
     * @return criterion
     */
    protected static CriterionForNonPrimitive<Comparable<Number>> produce(ConstrainedNode node, VId constraint) {
        return constraint.unsigned() ? initForUnsigned(node) : null;
    }

    /**
     * Produce criterion for unsigned.
     *
     * @param node constrained node
     * @return criterion for unsigned
     */
    private static CriterionForNonPrimitive<Comparable<Number>> initForUnsigned(ConstrainedNode node) {
        Class<?> clazz = TypeUtils.getClass(node.getType());
        Number max = UNSIGNED_MAX_VALUE_MAP.get(clazz);
        return new CriterionForNonPrimitive<Comparable<Number>>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Comparable<Number> value) {
                return le(value, max) ? null : new ValidationFailure(value);
            }
        };
    }

    /** Type({@code T extends Number & Comparable<T>}) -> unsigned max value map. */
    private static final Map<Class<?>, Number> UNSIGNED_MAX_VALUE_MAP;

    static {
        UNSIGNED_MAX_VALUE_MAP = new HashMap<>(4);
        UNSIGNED_MAX_VALUE_MAP.put(Long.class, 4294967295L);
        UNSIGNED_MAX_VALUE_MAP.put(BigInteger.class, new BigInteger("18446744073709551615"));
    }

}
