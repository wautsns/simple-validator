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
package com.github.wautsns.simplevalidator.constraint;

import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation constraint.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface AConstraint {

    /**
     * Get criterion factories of the constraint.
     *
     * @return criterion factories of the constraint
     */
    Class<? extends CriterionFactory>[] criterionFactories() default {};

    /**
     * Get combined constraints.
     *
     * @return combined constraints
     */
    ACombine[] combines() default {};

    /**
     * Get order of the constraint.
     *
     * @return order of the constraint.
     */
    int order() default 0;

}
