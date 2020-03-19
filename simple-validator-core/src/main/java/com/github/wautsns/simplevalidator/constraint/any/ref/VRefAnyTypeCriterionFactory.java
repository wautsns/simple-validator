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

import com.github.wautsns.simplevalidator.model.criterion.basic.Criteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.special.AbstractAnyTypeCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.processor.NodeCriterionProducer;
import com.github.wautsns.simplevalidator.model.node.ConstrainedClass;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.CriterionUtils;

import java.util.List;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VRefAnyTypeCriterionFactory extends AbstractAnyTypeCriterionFactory<VRef> {

    @Override
    public void process(ConstrainedNode node, VRef constraint, Criteria<Criterion> wip) {
        wip.add(produce(node, constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected static Criterion produce(ConstrainedNode node, VRef constraint) {
        ConstrainedClass refClass = ConstrainedClass.getInstance(constraint.value());
        String property = constraint.property();
        if (property.isEmpty()) { property = node.getLocation().getSimpleName(); }
        ConstrainedNode ref = refClass.requireChild(property);
        if (constraint.useRefTarget()) { return CriterionUtils.forNode(ref); }
        return new NodeCriterionProducer(initTmpConstrainedNode(node, ref)).produce();
    }

    private static ConstrainedNode initTmpConstrainedNode(ConstrainedNode node, ConstrainedNode ref) {
        return new ConstrainedNode(node.getLocation(), node.getType(), ref.getConstraints()) {
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
