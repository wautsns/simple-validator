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
package com.github.wautsns.simplevalidator.kernal.extractor.value.builtin.longvalue;

import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.LongValueExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.OptionalLong;

/**
 * Long value extractor for {@code OptionalDouble} value.
 *
 * @author wautsns
 * @since Mar 21, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LongValueExtractorForOptionalLong extends LongValueExtractor<OptionalLong> {

    /** {@code LongValueExtractorForOptionalLong} instance. */
    public static final LongValueExtractorForOptionalLong INSTANCE = new LongValueExtractorForOptionalLong();

    @Override
    public boolean applyTo(Type type) {
        return (type == OptionalLong.class);
    }

    @Override
    public String getNameOfExtractedValue() {
        return "";
    }

    @Override
    public long extract(OptionalLong target) {
        return target.orElseThrow(NullPointerException::new);
    }

}
