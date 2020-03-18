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
package com.github.wautsns.simplevalidator;

import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;
import com.github.wautsns.simplevalidator.model.node.extractedtype.ConstrainedExtractedType;
import com.github.wautsns.simplevalidator.model.node.ConstrainedTypeArg;
import com.github.wautsns.simplevalidator.util.ConstraintUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Simple validator configuration.
 *
 * @author wautsns
 * @since Mar 15, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleValidatorConfiguration {

    // -------------------- criterion factory --------------------------------------------

    /**
     * Add criterion factory.
     *
     * @param constraintClass constraint class
     * @param factory criterion factory
     * @param <A> type of constraint
     */
    public static <A extends Annotation> void addCriterionFactory(
            Class<A> constraintClass, CriterionFactory<A, ?, ?> factory) {
        addCriterionFactory(constraintClass, -1, factory);
    }

    /**
     * Add criterion factory.
     *
     * @param constraintClass constraint class
     * @param index index of the factory(negative index indicate counting from tail)
     * @param factory criterion factory
     * @param <A> type of constraint
     */
    public static <A extends Annotation> void addCriterionFactory(
            Class<A> constraintClass, int index, CriterionFactory<A, ?, ?> factory) {
        List<CriterionFactory<A, ?, ?>> factories = ConstraintUtils.getCriterionFactories(constraintClass);
        if (index < 0) { index = factories.size() + index; }
        if (!factories.isEmpty()) { factories.add(index, factory); }
        throw new IllegalArgumentException(String.format(
                "Constraint[%s] is a combined constraint, and cannot add criterion factory.",
                constraintClass));
    }

    // -------------------- constrained type arg factory --------------------------------

    /**
     * Add constrained type arg factory.
     *
     * @param factory constrained type arg factory
     * @see ConstrainedExtractedType.TypeArgsFactories#add(ConstrainedTypeArg.Factory)
     */
    public static void addConstrainedTypeArgFactory(ConstrainedTypeArg.Factory factory) {
        ConstrainedExtractedType.TypeArgsFactories.add(factory);
    }

}
