/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.wautsns.simplevalidator.constraint.number.domain;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.constraint.AVariableAlias;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactory;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.ValueExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.builtin.doublevlaue.DoubleValueExtractorForOptionalDouble;
import com.github.wautsns.simplevalidator.kernal.extractor.value.builtin.intvalue.IntValueExtractorForOptionalInt;
import com.github.wautsns.simplevalidator.kernal.extractor.value.builtin.longvalue.LongValueExtractorForOptionalLong;
import com.github.wautsns.templatemessage.formatter.multival.ArrayFormatter;
import com.github.wautsns.templatemessage.variable.Variable;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * VDomain.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
@AConstraint
public @interface VDomain {

    /**
     * Message(template).
     *
     * @return message(template)
     */
    String message() default "[`VDomain`]";

    /**
     * Order of the constraint.
     *
     * @return order of the constraint
     */
    int order() default 0;

    String[] value();

    // #################### extra #######################################################

    /** Built-in criterion factories. */
    List<CriterionFactory<VDomain, ?, ?>> CRITERION_FACTORIES = new LinkedList<>(Arrays.asList(
            VDomainCriterionFactoryForComparableNumber.INSTANCE,
            VDomainCriterionFactoryForNumericText.INSTANCE,
            VDomainCriterionFactoryForInt.INSTANCE,
            VDomainCriterionFactoryForLong.INSTANCE,
            VDomainCriterionFactoryForDouble.INSTANCE,
            VDomainCriterionFactoryForByte.INSTANCE,
            VDomainCriterionFactoryForShort.INSTANCE,
            VDomainCriterionFactoryForFloat.INSTANCE
    ));

    /** Built-in value extractors. */
    List<ValueExtractor> VALUE_EXTRACTORS = new LinkedList<>(Arrays.asList(
            IntValueExtractorForOptionalInt.INSTANCE,
            LongValueExtractorForOptionalLong.INSTANCE,
            DoubleValueExtractorForOptionalDouble.INSTANCE
    ));

    // ==================== variables ===================================================

    /** Variables: {@linkplain #value() domain}. */
    @AVariableAlias("value")
    Variable<String[]> DOMAIN = new Variable<>(
            "domain", new ArrayFormatter<>().setPrefix("").setSuffix("").setDelimiter("∪"));

}
