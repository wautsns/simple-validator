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
package com.github.wautsns.simplevalidator.model.node.extraction.value.optional;

import com.github.wautsns.simplevalidator.util.extractor.DoubleExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.OptionalDouble;

/**
 * The {@code DoubleExtractor} for {@code OptionalDouble}.
 *
 * @author wautsns
 * @since Mar 21, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DoubleExtractorForOptionalDouble implements DoubleExtractor<OptionalDouble> {

    /** {@code DoubleExtractorForOptionalDouble} instance. */
    public static final DoubleExtractorForOptionalDouble INSTANCE = new DoubleExtractorForOptionalDouble();

    @Override
    public boolean appliesTo(Type type) {
        return (type == OptionalDouble.class);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public double extract(OptionalDouble target) {
        return target.orElseThrow(NullPointerException::new);
    }

}
