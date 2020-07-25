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
import com.github.wautsns.simplevalidator.kernal.constraint.ConstraintMetadata;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactory;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.text.CriterionFactoryForTextLike;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.text.TextLikeUtility;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.CriterionFactoryForTimeLike;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.TimeLikeUtility;
import com.github.wautsns.simplevalidator.kernal.extractor.type.basic.AnnotatedTypeExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.ValueExtractor;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailureFormatter;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedParameter;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedTypeContainer;
import com.github.wautsns.simplevalidator.util.valuehandle.NumericTextParser;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Configuration of simple validator.
 *
 * @author wautsns
 * @since Mar 15, 2020
 */
@UtilityClass
public class SimpleValidatorConfiguration {

    /** Configuration for the constraint. */
    @UtilityClass
    public static class ForConstraint {

        /**
         * Add value extractor to make constraint support more types.
         *
         * <p>eg. I want to use &#64;VMin to validate value of type OptionalInt
         *
         * @param constraintType constraint type
         * @param valueExtractor value extractor
         */
        public static void addValueExtractor(
                Class<? extends Annotation> constraintType, ValueExtractor valueExtractor) {
            ConstraintMetadata.getInstance(constraintType).getValueExtractors().add(valueExtractor);
        }

        /**
         * Add criterion factory.
         *
         * <p>It is <strong>not recommended</strong> adding a criterion factory directly to the constraint annotation
         * like {@code VDomain.CRITERION_FACTORIES.add(...)}.
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

    }

    /** Configuration for the constrained node. */
    @UtilityClass
    public static class ForConstrainedNode {

        /**
         * Add extracted type metadata.
         *
         * @param order order
         * @param annotatedTypeExtractor extracted type metadata
         */
        public static void addAnnotatedTypeExtractor(int order, AnnotatedTypeExtractor annotatedTypeExtractor) {
            ConstrainedTypeContainer.addAnnotatedTypeExtractor(order, annotatedTypeExtractor);
        }

        /**
         * Set parameter name generator.
         *
         * @param parameterNameGenerator parameter name generator
         */
        public static void setParameterNameGenerator(Function<Parameter, String> parameterNameGenerator) {
            ConstrainedParameter.setParameterNameGenerator(parameterNameGenerator);
        }

    }

    /** Configuration for the type-like utility. */
    @UtilityClass
    public static class ForTypeLikeUtility {

        /**
         * Add text-like utility.
         *
         * @param textLikeUtility text-like utility
         */
        public static void addTextLikeUtility(TextLikeUtility<?> textLikeUtility) {
            CriterionFactoryForTextLike.UTILITIES.add(textLikeUtility);
        }

        /**
         * Add time-like utility.
         *
         * @param timeLikeUtility time-like utility
         */
        public static void addTimeLikeUtility(TimeLikeUtility<?> timeLikeUtility) {
            CriterionFactoryForTimeLike.UTILITIES.add(timeLikeUtility);
        }

    }

    /** Configuration for value. */
    @UtilityClass
    public static class ForValue {

        /**
         * Add numeric text parser.
         *
         * @param type numeric value type
         * @param parser text parser
         * @param <T> type of numeric value
         */
        public static <T extends Number & Comparable<T>> void addNumericTextParser(
                Class<T> type, Function<String, T> parser) {
            NumericTextParser.addParser(type, parser);
        }

    }

    /** Configuration for validation failure. */
    @UtilityClass
    public static class ForValidationFailure {

        /** Validation failure formatter. */
        public static final ValidationFailureFormatter FORMATTER = new ValidationFailureFormatter();

        /** Locale supplier. */
        @Getter
        @Setter
        private static Supplier<Locale> localeSupplier = Locale::getDefault;

    }

}
