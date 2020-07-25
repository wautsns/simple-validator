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
package com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Type-like utility criterion cache.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@SuppressWarnings("rawtypes")
public class TypeLikeUtilityCriterionCache<U extends TypeLikeUtility> {

    /** Cache. */
    private final Map<U, CriterionForNonPrimitive> cache = new ConcurrentHashMap<>(8);
    /** Criterion initializer. */
    private final Function<U, CriterionForNonPrimitive> initializer;

    /**
     * Construct a type-like utility criterion cache.
     *
     * @param initializer initializer for criterion
     */
    public TypeLikeUtilityCriterionCache(Function<U, CriterionForNonPrimitive> initializer) {
        this.initializer = initializer;
    }

    /**
     * Get criterion associated with the specified utility.
     *
     * @param utility utility
     * @param <T> type of value
     * @return criterion associated with the specified utility
     */
    @SuppressWarnings("unchecked")
    public final <T> CriterionForNonPrimitive<T> get(U utility) {
        return cache.computeIfAbsent(utility, initializer);
    }

}
