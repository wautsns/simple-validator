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
package com.github.wautsns.simplevalidator.constraint.any.indepth;

import com.github.wautsns.simplevalidator.Validator;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.special.NonPrimitiveCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.util.CriterionUtils;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Non-primitive criterion factory for in-depth validation.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VInDepthNonPrimitiveCriterionFactory extends NonPrimitiveCriterionFactory<VInDepth> {

    /** {@code VInDepthNonPrimitiveCriterionFactory} instance. */
    public static final VInDepthNonPrimitiveCriterionFactory INSTANCE = new VInDepthNonPrimitiveCriterionFactory();

    @Override
    public void process(ConstrainedNode node, VInDepth constraint, TCriteria<Object> wip) {
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
    protected TCriterion<Object> produce(ConstrainedNode node, VInDepth constraint) {
        return constraint.dynamic() ? CRITERION_FOR_DYNAMIC : produceForStatic(node);
    }

    /**
     * Produce criterion for static in-depth.
     *
     * @param node constrained node
     * @return criterion for static in-depth
     */
    private static TCriterion<Object> produceForStatic(ConstrainedNode node) {
        Class<?> clazz = TypeUtils.getClass(node.getType());
        TCriterion<Object> criterion = CriterionUtils.forType(clazz);
        return (criterion == TCriterion.TRUTH) ? null : criterion;
    }

    /** Criterion for dynamic in-depth. */
    private static final TCriterion<Object> CRITERION_FOR_DYNAMIC = Validator::validatePolitely;

}
