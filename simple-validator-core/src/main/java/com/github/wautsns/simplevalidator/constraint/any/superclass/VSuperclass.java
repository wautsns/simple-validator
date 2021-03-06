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
package com.github.wautsns.simplevalidator.constraint.any.superclass;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactory;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * VSuperclass.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@AConstraint
public @interface VSuperclass {

    /**
     * Order of the constraint.
     *
     * @return order of the constraint
     */
    int order() default 0;

    // #################### extra #######################################################

    /** Built-in criterion factories. */
    List<CriterionFactory<VSuperclass, ?, ?>> CRITERION_FACTORIES = new LinkedList<>(Collections.singletonList(
            VSuperclassCriterionFactoryForAnyNonPrimitive.INSTANCE
    ));

}
