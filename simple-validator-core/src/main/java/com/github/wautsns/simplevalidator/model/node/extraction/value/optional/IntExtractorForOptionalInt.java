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

import com.github.wautsns.simplevalidator.util.extractor.IntExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.OptionalInt;

/**
 * The {@code IntExtractor} for {@code OptionalInt}.
 *
 * @author wautsns
 * @since Mar 21, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntExtractorForOptionalInt implements IntExtractor<OptionalInt> {

    /** {@code IntExtractorForOptionalInt} instance. */
    public static final IntExtractorForOptionalInt INSTANCE = new IntExtractorForOptionalInt();

    @Override
    public boolean appliesTo(Type type) {
        return (type == OptionalInt.class);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int extract(OptionalInt target) {
        return target.orElseThrow(NullPointerException::new);
    }

}
