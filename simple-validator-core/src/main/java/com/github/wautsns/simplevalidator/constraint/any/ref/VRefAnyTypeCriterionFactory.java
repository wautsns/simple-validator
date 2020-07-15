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
package com.github.wautsns.simplevalidator.constraint.any.ref;

import com.github.wautsns.simplevalidator.model.constraint.Constraint;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.special.AnyTypeCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.util.CriterionUtils;
import com.github.wautsns.simplevalidator.model.node.ConstrainedClass;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.model.node.extraction.value.ConstrainedExtractedValue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VRefAnyTypeCriterionFactory extends AnyTypeCriterionFactory<VRef> {

    /** {@code VRefAnyTypeCriterionFactory} instance. */
    public static final VRefAnyTypeCriterionFactory INSTANCE = new VRefAnyTypeCriterionFactory();

    @Override
    public void process(ConstrainedNode node, VRef constraint, Criteria<Criterion> wip) {
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
    protected static Criterion produce(ConstrainedNode node, VRef constraint) {
        ConstrainedClass refClass = ConstrainedClass.getInstance(constraint.value());
        String property = constraint.property();
        if (property.isEmpty()) { property = node.getLocation().getSimpleName(); }
        ConstrainedNode ref = refClass.requireChild(property);
        if (constraint.useRefTarget()) { return CriterionUtils.forNode(ref); }
        return CriterionUtils.produce(initTmpConstrainedNode(node, ref));
    }

    /**
     * Initialize a <strong>temporary</strong> constrained node.
     *
     * <p>The temporary constrained node and original constrained node have the same type, parent and criterion
     * wrapper. And the temporary constrained node and reference constrained node have the same constraints, extracted
     * values and children.
     *
     * @param node original constrained node
     * @param ref reference constrained node
     * @return temporary constrained node
     */
    private static ConstrainedNode initTmpConstrainedNode(ConstrainedNode node, ConstrainedNode ref) {
        ConstrainedNode.Location location = node.getLocation();
        Type type = node.getType();
        List<Constraint<?>> constraints = ref.getConstraints();
        List<ConstrainedExtractedValue> extractedValues = ref.getExtractedValues();
        return new ConstrainedNode(location, type, constraints, extractedValues) {

            @Override
            public ConstrainedNode getParent() {
                return node.getParent();
            }

            @Override
            public List<? extends ConstrainedNode> getChildren() {
                return ref.getChildren();
            }

            @Override
            public Criterion.Wrapper getCriterionWrapper() {
                return node.getCriterionWrapper();
            }

        };
    }

}
