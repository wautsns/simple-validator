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
package com.github.wautsns.simplevalidator.kernal.extractor.value.builtin.intvalue;

import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.IntValueExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.OptionalInt;

/**
 * Int value extractor for {@code OptionalDouble} value.
 *
 * @author wautsns
 * @since Mar 21, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntValueExtractorForOptionalInt extends IntValueExtractor<OptionalInt> {

    /** {@code IntValueExtractorForOptionalInt} instance. */
    public static final IntValueExtractorForOptionalInt INSTANCE = new IntValueExtractorForOptionalInt();

    @Override
    public boolean applyTo(Type type) {
        return (type == OptionalInt.class);
    }

    @Override
    public String getNameOfExtractedValue() {
        return "";
    }

    @Override
    public int extract(OptionalInt target) {
        return target.orElseThrow(NullPointerException::new);
    }

}
