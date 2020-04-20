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
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.text.TextLikeCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.text.TextLikeUtility;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.TimeLikeCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.TimeLikeUtility;
import com.github.wautsns.simplevalidator.model.node.ConstrainedParameter;
import com.github.wautsns.simplevalidator.model.node.ConstrainedTypeContainer;
import com.github.wautsns.simplevalidator.model.node.extraction.type.ConstrainedExtractedType;
import com.github.wautsns.simplevalidator.util.common.NumericTextParser;
import com.github.wautsns.simplevalidator.util.extractor.ValueExtractor;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

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
@UtilityClass
public class SimpleValidatorConfiguration {

    /** configuration for criterion factory */
    @UtilityClass
    public static class ForConstraint {

        /**
         * Add criterion factory.
         *
         * <pre>
         * // It is <strong>not recommended</strong> adding a criterion factory directly to the constraint annotation.
         * <s>VLuhn.CRITERION_FACTORIES.add(criterionFactory);</s>
         * SimpleValidatorConfiguration.ForCriterionFactory.add(VLuhn.class, criterionFactory);
         * </pre>
         *
         * @param constraintType constraint class
         * @param criterionFactory criterion factory
         * @param <A> type of constraint
         */
        public static <A extends Annotation> void addCriterionFactory(
                Class<A> constraintType, @NonNull CriterionFactory<A, ?, ?> criterionFactory) {
            ConstraintMetadata<A> metadata = ConstraintMetadata.getInstance(constraintType);
            if (metadata.isOnlyUsedToCombineOtherConstraints()) {
                throw new ConstraintAnalysisException(
                        "[%s] cannot add criterion factory, because it is only used to combine other constraints.",
                        constraintType);
            }
            metadata.getCriterionFactories().add(Objects.requireNonNull(criterionFactory));
        }

        /**
         * Add value extractor.
         *
         * @param constraintType constraint type
         * @param valueExtractor value extractor
         */
        public static void addValueExtractor(
                Class<? extends Annotation> constraintType, @NonNull ValueExtractor valueExtractor) {
            ConstraintMetadata.getInstance(constraintType).getValueExtractors().add(valueExtractor);
        }

    }

    /** configuration for constrained node */
    @UtilityClass
    public static class ForConstrainedNode {

        /**
         * Add extracted type metadata.
         *
         * @param order order
         * @param extractedTypeMetadata extracted type metadata
         * @see ConstrainedTypeContainer#addExtractedTypeMetadata(int, ConstrainedExtractedType.Metadata)
         */
        public static void addExtractedTypeMetadata(
                int order, ConstrainedExtractedType.Metadata extractedTypeMetadata) {
            ConstrainedTypeContainer.addExtractedTypeMetadata(order, extractedTypeMetadata);
        }

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

    /** configuration for type like utility */
    @UtilityClass
    public static class ForTypeLikeUtility {

        /**
         * Add text like utility.
         *
         * @param textLikeUtility text like utility
         */
        public static void addTextLikeUtility(@NonNull TextLikeUtility<?> textLikeUtility) {
            TextLikeCriterionFactory.DEFAULT_UTILITIES.add(textLikeUtility);
        }

        /**
         * Add time like utility.
         *
         * @param timeLikeUtility time like utility
         */
        public static void addTimeLikeUtility(@NonNull TimeLikeUtility<?> timeLikeUtility) {
            TimeLikeCriterionFactory.DEFAULT_UTILITIES.add(timeLikeUtility);
        }

    }

    /** configuration for value handler */
    @UtilityClass
    public static class ForValueHandler {

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

}
