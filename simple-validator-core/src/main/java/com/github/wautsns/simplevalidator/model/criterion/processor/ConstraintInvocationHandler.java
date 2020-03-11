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
package com.github.wautsns.simplevalidator.model.criterion.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * Constraint invocation handler.
 *
 * <p>Used to proxy constraint.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
class ConstraintInvocationHandler implements InvocationHandler {

    /** constraint annotation type */
    private final Class<? extends Annotation> type;
    /** annotation attributes map */
    private final LinkedHashMap<String, Object> memberValues;

    public ConstraintInvocationHandler(Class<? extends Annotation> type, LinkedHashMap<String, Object> memberValues) {
        this.type = type;
        this.memberValues = memberValues;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Modifier.isStatic(method.getModifiers())) {
            return method.invoke(null, args);
        }
        String methodName = method.getName();
        switch (methodName) {
            case "hashCode":
                return hashCode();
            case "toString":
                return toString();
            case "annotationType":
                return type;
            case "equals":
                return equals(args[0]);
            default:
                return memberValues.get(methodName);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('@').append(type.getName());
        result.append('(');
        String attributes = memberValues.entrySet().stream()
                .map(e -> e.getKey() + '=' + e.getValue())
                .collect(Collectors.joining(", "));
        result.append(attributes);
        result.append(')');
        return result.toString();
    }

}
