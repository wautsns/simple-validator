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
 * The {@code short} value extractor.
 *
 * @param <T> applicable type
 * @author wautsns
 * @since Mar 20, 2020
 */
public interface ShortExtractor<T> extends ValueExtractor {

    @Override
    default Type getExtractedValueType() {
        return short.class;
    }

    /**
     * Extract {@code short} value from the given source.
     *
     * @param source source
     * @return extracted {@code short} value
     */
    short extract(T source);

}
