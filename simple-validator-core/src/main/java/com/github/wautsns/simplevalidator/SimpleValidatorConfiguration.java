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
import com.github.wautsns.simplevalidator.model.node.ConstrainedParameter;
import com.github.wautsns.simplevalidator.model.node.ConstrainedTypeContainer;
import com.github.wautsns.simplevalidator.model.node.extractedtype.ConstrainedExtractedType;
import com.github.wautsns.simplevalidator.util.ConstraintUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Function;

/**
 * Simple validator configuration.
 *
 * @author wautsns
 * @since Mar 15, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleValidatorConfiguration {

    // -------------------- criterion factory -------------------------------------------

    /**
     * Add criterion factory.
     *
     * @param constraint constraint class
     * @param factory criterion factory
     * @param <A> type of constraint
     */
    public static <A extends Annotation> void addCriterionFactory(
            Class<A> constraint, CriterionFactory<A, ?, ?> factory) {
        addCriterionFactory(constraint, -1, factory);
    }

    /**
     * Add criterion factory.
     *
     * @param constraint constraint class
     * @param index index of the factory(negative index indicate counting from tail)
     * @param factory criterion factory
     * @param <A> type of constraint
     */
    public static <A extends Annotation> void addCriterionFactory(
            Class<A> constraint, int index, CriterionFactory<A, ?, ?> factory) {
        List<CriterionFactory<A, ?, ?>> factories = ConstraintUtils.getCriterionFactories(constraint);
        if (index < 0) { index = factories.size() + index; }
        if (!factories.isEmpty()) { factories.add(index, factory); }
        throw new IllegalArgumentException(String.format(
                "Constraint[%s] is a combined constraint, and cannot addCriterionFactory criterion factory.",
                constraint));
    }

    // -------------------- extracted type metadata -------------------------------------

    /**
     * Add extracted type metadata.
     *
     * @param order order
     * @param metadata extracted type metadata
     * @see ConstrainedTypeContainer#addTypeExtractedMetadata(int, ConstrainedExtractedType.Metadata)
     */
    public static void addConstrainedExtractedTypeMetadata(int order, ConstrainedExtractedType.Metadata metadata) {
        ConstrainedTypeContainer.addTypeExtractedMetadata(order, metadata);
    }

    // -------------------- parameter ---------------------------------------------------

    /**
     * Set parameter name generator.
     *
     * @param parameterNameGenerator parameter name generator
     * @see ConstrainedParameter#setParameterNameGenerator(Function)
     */
    public static void setParameterNameGenerator(Function<Parameter, String> parameterNameGenerator) {
        ConstrainedParameter.setParameterNameGenerator(parameterNameGenerator);
    }

}
