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
package com.github.wautsns.simplevalidator.model.criterion.factory;

import com.github.wautsns.simplevalidator.model.criterion.basic.Criteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

import java.lang.annotation.Annotation;

/**
 * Criterion factory.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public interface CriterionFactory<A extends Annotation, W extends Criteria<C>, C extends Criterion> {

    /**
     * Return {@code true} if the factory applies to the constraint on the node, otherwise {@code false}.
     *
     * @param node constrained node
     * @param constraint constraint
     * @return {@code true} if the factory applies to the constraint on the node, otherwise {@code false}
     */
    boolean appliesTo(ConstrainedNode node, A constraint);

    /**
     * Process the criterion wip.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip criterion wip
     */
    void process(ConstrainedNode node, A constraint, W wip);

}
