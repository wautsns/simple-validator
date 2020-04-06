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
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation variable alias.
 *
 * <pre>
 * // example
 * ...
 * public @interface VMax {
 *     ...
 *     String value();
 *     ...
 *     // ===== variables ==============
 *     &#64;AVariableAlias("value")
 *     Variable&lt;String&gt; MAX = new Variable("max");
 * }
 * </pre>
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface AVariableAlias {

    /**
     * Alias of variable.
     *
     * @return alias of variable
     */
    String value();

}
