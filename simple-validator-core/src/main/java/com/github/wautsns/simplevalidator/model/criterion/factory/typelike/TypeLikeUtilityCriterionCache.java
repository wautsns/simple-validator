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
package com.github.wautsns.simplevalidator.model.criterion.factory.typelike;

import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Type like utility criterion cache.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
public class TypeLikeUtilityCriterionCache<U extends TypeLikeUtility> {

    /** cache */
    private final @NonNull
    Map<U, TCriterion> cache = new ConcurrentHashMap<>(8);
    /** criterion initializer */
    private final @NonNull
    Function<U, TCriterion> initializer;

    /**
     * Get criterion associated with the utility.
     *
     * @param utility utility
     * @return criterion associated with the utility
     */
    @SuppressWarnings("unchecked")
    public <T> TCriterion<T> get(U utility) {
        return cache.computeIfAbsent(utility, initializer);
    }

}