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
package com.github.wautsns.simplevalidator.constraint.any.superclass;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForAnyNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.util.CriterionUtils;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * VSuperclass criterion factory for any non-primitive value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VSuperclassCriterionFactoryForAnyNonPrimitive extends CriterionFactoryForAnyNonPrimitive<VSuperclass> {

    /** {@code VSuperclassCriterionFactoryForAnyNonPrimitive} instance. */
    public static final VSuperclassCriterionFactoryForAnyNonPrimitive INSTANCE = new VSuperclassCriterionFactoryForAnyNonPrimitive();

    @Override
    public void process(ConstrainedNode node, VSuperclass constraint, CriteriaForNonPrimitive<Object> wip) {
        wip.add(produce(node));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param node constrained node
     * @return criterion
     */
    protected CriterionForNonPrimitive<Object> produce(ConstrainedNode node) {
        Class<?> superclass = ((Class<?>) node.getType()).getSuperclass();
        return CriterionUtils.getForType(Objects.requireNonNull(superclass));
    }

}
