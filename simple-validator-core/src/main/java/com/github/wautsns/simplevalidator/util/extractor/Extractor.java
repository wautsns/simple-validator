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
 * Extractor for extracting value from target value.
 *
 * <ul>
 * implementations:
 * <li>{@code boolean}: {@link BooleanExtractor}</li>
 * <li>{@code byte}: {@link ByteExtractor}</li>
 * <li>{@code char}: {@link CharExtractor}</li>
 * <li>{@code double}: {@link DoubleExtractor}</li>
 * <li>{@code float}: {@link FloatExtractor}</li>
 * <li>{@code int}: {@link IntExtractor}</li>
 * <li>{@code long}: {@link LongExtractor}</li>
 * <li>{@code short}: {@link ShortExtractor}</li>
 * <li>{@code non-primitive}: {@link TExtractor}</li>
 * </ul>
 *
 * @author wautsns
 * @since Mar 21, 2020
 */
public interface Extractor {

    /**
     * Return whether the extractor applies to the specified type.
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
