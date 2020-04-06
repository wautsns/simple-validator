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

import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.model.constraint.ConstraintMetadata;
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;
import com.github.wautsns.simplevalidator.model.node.ConstrainedParameter;
import com.github.wautsns.simplevalidator.model.node.ConstrainedTypeContainer;
import com.github.wautsns.simplevalidator.model.node.extraction.type.ConstrainedExtractedType;
import com.github.wautsns.simplevalidator.util.common.NumericTextParser;
import com.github.wautsns.simplevalidator.util.extractor.ValueExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.function.Function;

/**
 * Configuration of simple validator.
 *
 * @author wautsns
 * @since Mar 15, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleValidatorConfiguration {

    // #################### criterion factory ###########################################

    /**
     * Add criterion factory.
     *
     * @param constraintType constraint class
     * @param criterionFactory criterion factory
     * @param <A> type of constraint
     */
    public static <A extends Annotation> void addCriterionFactory(
            Class<A> constraintType, CriterionFactory<A, ?, ?> criterionFactory) {
        ConstraintMetadata<A> metadata = ConstraintMetadata.getInstance(constraintType);
        if (metadata.isOnlyUsedToCombineOtherConstraints()) {
            throw new ConstraintAnalysisException(
                    "[%s] cannot add criterion factory, because it is only used to combine other constraints.",
                    constraintType);
        }
        metadata.getCriterionFactories().add(Objects.requireNonNull(criterionFactory));
    }

    // #################### extracted type metadata #####################################

    /**
     * Add extracted type metadata.
     *
     * @param order order
     * @param extractedTypeMetadata extracted type metadata
     * @see ConstrainedTypeContainer#addExtractedTypeMetadata(int, ConstrainedExtractedType.Metadata)
     */
    public static void addExtractedTypeMetadata(int order, ConstrainedExtractedType.Metadata extractedTypeMetadata) {
        ConstrainedTypeContainer.addExtractedTypeMetadata(order, extractedTypeMetadata);
    }

    // #################### parameter ###################################################

    /**
     * Set parameter name generator.
     *
     * @param parameterNameGenerator parameter name generator
     * @see ConstrainedParameter#setParameterNameGenerator(Function)
     */
    public static void setParameterNameGenerator(Function<Parameter, String> parameterNameGenerator) {
        ConstrainedParameter.setParameterNameGenerator(parameterNameGenerator);
    }

    // #################### value handler ###############################################

    /**
     * Add value extractor.
     *
     * @param constraintType constraint type
     * @param valueExtractor value extractor
     */
    public static void addValueExtractor(Class<? extends Annotation> constraintType, ValueExtractor valueExtractor) {
        ConstraintMetadata.getInstance(constraintType).getValueExtractors().add(valueExtractor);
    }

    /**
     * Add numeric text parser.
     *
     * @param type numeric value type
     * @param parser text parser
     * @param <T> type of numeric value
     * @see NumericTextParser#addParser(Class, Function)
     */
    public static <T extends Number & Comparable<T>> void addNumericValueParser(
            Class<T> type, Function<String, T> parser) {
        NumericTextParser.addParser(type, parser);
    }

}
