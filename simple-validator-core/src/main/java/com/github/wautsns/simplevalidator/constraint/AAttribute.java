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

    /**
     * Name of the attribute.
     *
     * @return name of the target attribute
     */
    String name();

    /**
     * Value spel, default is {@code LOOK_VALUE}.
     *
     * <p>Use {@link #LOOK_VALUE} to look {@link #values()}.
     *
     * @return value spel
     */
    String spel() default LOOK_VALUE;

    /**
     * Values(in string format), default is <code>{LOOK_REF}</code>.
     *
     * <p>Use {@link #LOOK_REF} to look {@link #ref()}.
     *
     * @return values(in string format)
     */
    String[] values() default { LOOK_REF };

    /**
     * Value ref, default is {@code ""}.
     *
     * <p>Use {@code ""} if the ref is equal to {@link #name()}.
     *
     * @return value ref
     */
    String ref() default "";

    // #################### special values ##############################################

    /** Look value, is used for {@link #spel()}. */
    String LOOK_VALUE = "LOOK_VALUE##AAttribute.constraint.simple-validator.wautsns.github.com";

    /** Look ref, is used for {@link #values()}. */
    String LOOK_REF = "LOOK_REF#AAttribute.constraint.simple-validator.wautsns.github.com";

    /** Use default value, is used for {@link #values()}. */
    String DEFAULT = "DEFAULT#AAttribute.constraint.simple-validator.wautsns.github.com";

}
