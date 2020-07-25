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

import com.github.wautsns.simplevalidator.kernal.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.Criteria;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForAnyType;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * VWithSpel criterion factory for any type.
 *
 * @author wautsns
 * @since Mar 15, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VWithSpelCriterionFactoryForAnyType extends CriterionFactoryForAnyType<VWithSpel> {

    /** {@code VWithSpelCriterionFactoryForAnyType} instance. */
    public static final VWithSpelCriterionFactoryForAnyType INSTANCE = new VWithSpelCriterionFactoryForAnyType();

    @Override
    public void process(ConstrainedNode node, VWithSpel constraint, Criteria<Criterion> wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /** Spel parser. */
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    /** Spel context. */
    private static final StandardEvaluationContext CTX = new StandardEvaluationContext();

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @return criterion
     */
    protected static CriterionForNonPrimitive<?> produce(VWithSpel constraint) {
        String expr = constraint.expr();
        SpelExpression spel = PARSER.parseRaw(expr);
        return new CriterionForNonPrimitive<Object>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Object value) {
                return Boolean.TRUE.equals(spel.getValue(value)) ? null : new ValidationFailure(value);
            }
        };
    }

}
