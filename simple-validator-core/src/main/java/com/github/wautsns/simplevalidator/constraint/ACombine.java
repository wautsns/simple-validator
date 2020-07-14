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

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation combine.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@Documented
@Retention(RUNTIME)
public @interface ACombine {

    /**
     * Constraint type.
     *
     * @return constraint type
     */
    Class<? extends Annotation> constraint();

    /**
     * Message for the constraint.
     *
     * @return message
     * @see com.github.wautsns.simplevalidator.model.constraint.ConstraintMetadata.Attributes#MESSAGE
     */
    String message() default "";

    /**
     * Order of the {@code ACombine}.
     *
     * @return order of the {@code ACombine}
     */
    int order() default 0;

    /**
     * Attributes of the constraint.
     *
     * @return attributes of the constraint
     */
    AAttribute[] attributes() default {};

}
