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

import com.github.wautsns.simplevalidator.util.extractor.LongExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.OptionalLong;

/**
 * {@code LongExtractor} for {@code OptionalLong}.
 *
 * @author wautsns
 * @since Mar 21, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LongExtractorForOptionalLong implements LongExtractor<OptionalLong> {

    /** {@code LongExtractorForOptionalLong} instance */
    public static final LongExtractorForOptionalLong INSTANCE = new LongExtractorForOptionalLong();

    @Override
    public long extract(OptionalLong target) {
        return target.orElseThrow(NullPointerException::new);
    }

    @Override
    public boolean appliesTo(Type type) {
        return type == OptionalLong.class;
    }

    @Override
    public String getName() {
        return "";
    }

}