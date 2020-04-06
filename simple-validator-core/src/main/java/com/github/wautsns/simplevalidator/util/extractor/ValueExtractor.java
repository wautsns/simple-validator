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
package com.github.wautsns.simplevalidator.util.extractor;

import java.lang.reflect.Type;

/**
 * Value extractor.
 *
 * @author wautsns
 * @since Mar 21, 2020
 */
public interface ValueExtractor {

    /**
     * Whether the extractor applies to the specified type.
     *
     * @param type type
     * @return {@code true} if the extractor applies to the specified type, otherwise {@code false}
     */
    boolean appliesTo(Type type);

    /**
     * Get name of the extracted value.
     *
     * @return name of the extracted value
     */
    String getName();

    /**
     * Get extracted value type.
     *
     * @return extracted value type
     */
    Type getExtractedValueType();

}
