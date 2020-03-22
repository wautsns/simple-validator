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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation attribute.
 *
 * <p>Used to specify attribute in {@linkplain ACombine @ACombine}.
 *
 * <p>Priority: {@linkplain #spel() spel}, {@linkplain #values() value}, {@linkplain #ref() ref}.
 *
 * @author wautsns
 * @see ACombine
 * @since Mar 11, 2020
 */
@Documented
@Retention(RUNTIME)
public @interface AAttribute {

    String LOOK_VALUE = "LOOK_VALUE##AAttribute.constraint.simple-validator.wautsns.github.com";

    String LOOK_REF = "LOOK_REF#AAttribute.constraint.simple-validator.wautsns.github.com";

    String DEFAULT = "DEFAULT#AAttribute.constraint.simple-validator.wautsns.github.com";

    /**
     * Get name of the attribute.
     *
     * @return name of the attribute
     */
    String name();

    /**
     * Set value by spel.
     *
     * @return set value by spel
     */
    String spel() default LOOK_VALUE;

    /**
     * Set value by strings
     *
     * @return set value by strings
     */
    String[] values() default {LOOK_REF};

    /**
     * Set value by a ref.
     *
     * @return set value by a ref
     */
    String ref() default "";

}
