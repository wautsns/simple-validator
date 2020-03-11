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
package com.github.wautsns.simplevalidator.util;

import com.github.wautsns.simplevalidator.model.criterion.kernel.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.processor.NodeCriterionProducer;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class CriterionUtils {

    /** ConcurrentHashMap can't put null value. */
    private static final TCriterion<?> NULL = tmp -> null;
    private static final Map<Class, Criterion> CACHE_FOR_CLASS = new ConcurrentHashMap<>(64);
    private static final Map<ConstrainedNode, Criterion> CACHE_FOR_CLASS_ELEMENTS = new ConcurrentHashMap<>(128);

    public static <C extends Criterion> C forClass(Class<?> clazz) {
        C criterion = (C) CACHE_FOR_CLASS.computeIfAbsent(clazz, CriterionUtils::resolveClass);
        return (criterion == NULL) ? null : criterion;
    }

    public static <C extends Criterion> C forParameter(Parameter parameter) {
        C criterion = resolveElement(ConstrainedNodeUtils.forParameter(parameter));
        return (criterion == NULL) ? null : criterion;
    }

    public static <C extends Criterion> C forElement(ConstrainedNode element) {
        switch (element.getCategory()) {
            case TYPE:
                return forClass((Class<?>) element.getOrigin());
            case PARAMETER:
                return forParameter((Parameter) element.getOrigin());
            default:
                return (C) CACHE_FOR_CLASS_ELEMENTS.computeIfAbsent(element, CriterionUtils::resolveElement);
        }
    }

    private static <C extends Criterion> C resolveClass(Class<?> clazz) {
        return resolveElement(ConstrainedNodeUtils.forClass(clazz));
    }

    private static <C extends Criterion> C resolveElement(ConstrainedNode element) {
        C criterion = new NodeCriterionProducer(element).produce();
        return (criterion == null) ? (C) NULL : criterion;
    }

    private CriterionUtils() {}

}
