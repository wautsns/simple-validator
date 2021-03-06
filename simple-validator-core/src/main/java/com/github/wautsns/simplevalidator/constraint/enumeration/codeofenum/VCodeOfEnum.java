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
package com.github.wautsns.simplevalidator.constraint.enumeration.codeofenum;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactory;
import com.github.wautsns.simplevalidator.kernal.failure.Formatters;
import com.github.wautsns.templatemessage.variable.Variable;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * VCodeOfEnum.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
@AConstraint
public @interface VCodeOfEnum {

    /**
     * Message(template).
     *
     * @return message(template)
     */
    String message() default "[`VCodeOfEnum`]";

    /**
     * Order of the constraint.
     *
     * @return order of the constraint
     */
    int order() default 0;

    Class<? extends Enum<? extends CodableEnum<?>>> value();

    String[] include() default {};

    String[] exclude() default {};

    // #################### extra #######################################################

    /** Built-in criterion factories. */
    List<CriterionFactory<VCodeOfEnum, ?, ?>> CRITERION_FACTORIES = new LinkedList<>(Collections.singletonList(
            VCodeOfEnumCriterionFactoryForAnyType.INSTANCE
    ));

    // ==================== variables ===================================================

    /** Variables: optionalValues. */
    Variable<Object[]> OPTIONAL_VALUES = new Variable<>("optionalValues", Formatters.VALUES_NO_OPTIONAL_VALUE);

}
