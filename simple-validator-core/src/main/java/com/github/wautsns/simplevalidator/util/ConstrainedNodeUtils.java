/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.wautsns.simplevalidator.util;

import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utils for the node with constraints.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class ConstrainedNodeUtils {

    private static final Map<Class<?>, ConstrainedNode> CACHE_FOR_CLASS =
            new ConcurrentHashMap<>(64);

    public static ConstrainedNode forClass(Class<?> clazz) {
        return CACHE_FOR_CLASS.computeIfAbsent(clazz, ConstrainedNode::new);
    }

    public static ConstrainedNode forParameter(Parameter parameter) {
        return new ConstrainedNode(parameter);
    }

    private ConstrainedNodeUtils() {}

}
