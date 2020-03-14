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
package com.github.wautsns.simplevalidator.constraint.number.min;

import com.github.wautsns.simplevalidator.constraint.AAttribute;
import com.github.wautsns.simplevalidator.constraint.ACombine;
import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.constraint.AVariableAlias;
import com.github.wautsns.simplevalidator.constraint.number.domain.VDomain;
import com.github.wautsns.templatemessage.variable.Variable;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE})
@AConstraint(combines = {
        @ACombine(constraint = VDomain.class, attributes = {
                @AAttribute(name = "value", spel = "(inclusive ? '[' : '(') + value + ',~)'")
        })
})
public @interface VMin {

    String message() default "[`VMin`]";

    int order() default 0;

    String value();

    boolean inclusive() default true;

    // ------------------------- variables -----------------------------------------

    @AVariableAlias("value")
    Variable<String> MIN = new Variable<>("min");
    Variable<String> INCLUSIVE = new Variable<>("inclusive");

}
