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

import com.github.wautsns.templatemessage.variable.Variable;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Type like utility variable cache.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
public class TypeLikeUtilityVariableCache<U extends TypeLikeUtility> {

    /** Cache. */
    private final Map<U, Variable> cache = new ConcurrentHashMap<>(8);
    /** Name of variable. */
    private final String name;

    /**
     * Get variable associated with the specified utility.
     *
     * @param utility utility
     * @return variable associated with the specified utility
     */
    @SuppressWarnings("unchecked")
    public <T> Variable<T> get(U utility) {
        return cache.computeIfAbsent(utility, u -> u.initTypeLikeVariable(name));
    }

}
