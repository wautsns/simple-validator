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
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForAnyNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.util.CriterionUtils;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.function.UnaryOperator;

/**
 * VInDepth criterion factory for any non-primitive value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VInDepthCriterionFactoryForAnyNonPrimitive extends CriterionFactoryForAnyNonPrimitive<VInDepth> {

    /** {@code VInDepthCriterionFactoryForAnyNonPrimitive} instance. */
    public static final VInDepthCriterionFactoryForAnyNonPrimitive INSTANCE = new VInDepthCriterionFactoryForAnyNonPrimitive();

    @Override
    public void process(ConstrainedNode node, VInDepth constraint, CriteriaForNonPrimitive<Object> wip) {
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
    protected CriterionForNonPrimitive<Object> produce(ConstrainedNode node, VInDepth constraint) {
        return constraint.dynamic() ? produceForDynamic(node) : produceForStatic(node);
    }

    /**
     * Produce criterion for dynamic in-depth.
     *
     * @param node constrained node
     * @return criterion for dynamic in-depth
     */
    private static CriterionForNonPrimitive<Object> produceForDynamic(ConstrainedNode node) {
        UnaryOperator<ValidationFailure> failureEnhancer = initVInDepthFailureEnhancer(node);
        return new CriterionForNonPrimitive<Object>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Object value) {
                ValidationFailure failure = Validator.validatePolitely(value);
                if (failure == null) { return null; }
                return failureEnhancer.apply(failure);
            }
        };
    }

    /**
     * Produce criterion for static in-depth.
     *
     * @param node constrained node
     * @return criterion for static in-depth
     */
    private static CriterionForNonPrimitive<Object> produceForStatic(ConstrainedNode node) {
        Class<?> clazz = TypeUtils.getClass(node.getType());
        CriterionForNonPrimitive<Object> criterion = CriterionUtils.getForType(clazz);
        criterion.addValidationFailureEnhancer(initVInDepthFailureEnhancer(node));
        return criterion;
    }

    /**
     * Initialize VInDepth failure enhancer.
     *
     * @param node constrained node
     * @return VInDepth failure enhancer
     */
    private static UnaryOperator<ValidationFailure> initVInDepthFailureEnhancer(ConstrainedNode node) {
        LinkedList<String> nodeNames = node.getLocation().copyNodeNames();
        return failure -> {
            LinkedList<String> subNodeNames = failure
                    .getValue(ValidationFailure.Variables.LOCATION)
                    .copyNodeNames();
            subNodeNames.removeFirst();
            subNodeNames.addAll(0, nodeNames);
            ConstrainedNode.Location subLocation = new ConstrainedNode.Location(subNodeNames);
            failure.put(ValidationFailure.Variables.LOCATION, subLocation);
            return failure;
        };
    }

}
