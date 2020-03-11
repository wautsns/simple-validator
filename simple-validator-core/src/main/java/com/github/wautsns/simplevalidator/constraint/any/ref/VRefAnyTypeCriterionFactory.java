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

import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.model.criterion.factory.special.AbstractAnyTypeCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.kernel.Criteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.processor.NodeCriterionProducer;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.ConstrainedNodeUtils;
import com.github.wautsns.simplevalidator.util.CriterionUtils;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VRefAnyTypeCriterionFactory extends AbstractAnyTypeCriterionFactory<VRef> {

    @Override
    public void process(ConstrainedNode element, VRef constraint, Criteria<Criterion> wip) {
        wip.add(produce(element, constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected static Criterion produce(ConstrainedNode element, VRef constraint) {
        ConstrainedNode refClass = ConstrainedNodeUtils.forClass(constraint.value());
        String refAttrName = getRefAttrName(element, constraint);
        ConstrainedNode ref = refClass.getChild(refAttrName);
        if (constraint.useRefTarget()) { return CriterionUtils.forElement(ref); }
        ConstrainedNode tmp = initTmpConstrainedNode(element, ref);
        return new NodeCriterionProducer(tmp).produce();
    }

    private static String getRefAttrName(ConstrainedNode element, VRef constraint) {
        String attr = constraint.attr();
        if (!attr.isEmpty()) { return '#' + attr; }
        switch (element.getCategory()) {
            case FIELD:
            case GETTER:
                return element.getName();
            default:
        }
        // TODO message
        throw new ConstraintAnalysisException("");
    }

    private static ConstrainedNode initTmpConstrainedNode(
            ConstrainedNode element, ConstrainedNode ref) {
        // TODO check inferiors of element and ref
        AnnotatedType annotatedType;
        if (ref.getCategory() == ConstrainedNode.Category.FIELD) {
            annotatedType = ((Field) ref.getOrigin()).getAnnotatedType();
        } else {
            annotatedType = ((Method) ref.getOrigin()).getAnnotatedReturnType();
        }
        return new ConstrainedNode(element.getCategory(), element.getParent(), element.getOrigin(), annotatedType);
    }

}
