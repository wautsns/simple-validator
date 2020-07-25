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
package com.github.wautsns.simplevalidator.kernal.extractor.value.basic;

import java.lang.reflect.Type;

/**
 * Value extractor.
 *
 * @author wautsns
 * @see BooleanValueExtractor
 * @see CharValueExtractor
 * @see ByteValueExtractor
 * @see ShortValueExtractor
 * @see IntValueExtractor
 * @see LongValueExtractor
 * @see FloatValueExtractor
 * @see DoubleValueExtractor
 * @see NonPrimitiveValueExtractor
 * @since Mar 21, 2020
 */
public abstract class ValueExtractor {

    /**
     * Return whether the value extractor applies to the specified type.
     *
     * @param type type
     * @return {@code true} if the value extractor applies to the specified type, otherwise {@code false}
     */
    public abstract boolean applyTo(Type type);

    /**
     * Get name of extracted value.
     *
     * <p>Return an empty string, if no need to perceive the extraction process. eg. OptionalInt -&gt; int
     * <br>Otherwise, return a string started with "#". eg. "#size"
     *
     * @return name of extracted value
     */
    public abstract String getNameOfExtractedValue();

    /**
     * Get type of extracted value.
     *
     * @return type of extracted value
     */
    public abstract Type getTypeOfExtractedValue();

}
