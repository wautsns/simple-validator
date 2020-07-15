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
package com.github.wautsns.simplevalidator.constraint.any.spel;

import com.github.wautsns.simplevalidator.model.criterion.basic.Criteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.special.AnyTypeCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author wautsns
 * @since Mar 15, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VWithSpelAnyTypeCriterionFactory extends AnyTypeCriterionFactory<VWithSpel> {

    /** {@code VWithSpelAnyTypeCriterionFactory} instance. */
    public static final VWithSpelAnyTypeCriterionFactory INSTANCE = new VWithSpelAnyTypeCriterionFactory();

    @Override
    public void process(ConstrainedNode node, VWithSpel constraint, Criteria<Criterion> wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    protected static TCriterion<?> produce(VWithSpel constraint) {
        String expr = constraint.expr();
        Expression expression = PARSER.parseExpression(expr);
        return value -> Boolean.TRUE.equals(expression.getValue(value, boolean.class)) ? null
                : new ValidationFailure(value);
    }

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

}
