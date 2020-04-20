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

import java.util.Objects;

/**
 * Codable enumeration.
 *
 * @param <T> type of code
 * @author wautsns
 * @since Mar 11, 2020
 */
public interface CodableEnum<T> {

    /***
     * Get code.
     * @return code
     */
    T getCode();

    /**
     * Return whether the specified code is code of the enumeration.
     *
     * @param code code
     * @return {@code true} if the specified code is code of the enumeration, otherwise {@code false}
     */
    default boolean equalsToCode(T code) {
        return Objects.deepEquals(getCode(), code);
    }

}
