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
package com.github.wautsns.simplevalidator.util.common;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * Annotation proxy.
 *
 * @author wautsns
 * @since Mar 21, 2020
 */
@UtilityClass
public class AnnotationProxy {

    /**
     * Proxy an annotation of specified type and attributes.
     *
     * @param annotationType annotation type
     * @param memberValues attributes
     * @param <A> type of annotation
     * @return annotation
     */
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A proxy(Class<A> annotationType, LinkedHashMap<String, Object> memberValues) {
        return (A) Proxy.newProxyInstance(
                annotationType.getClassLoader(),
                new Class[]{ annotationType },
                new ConstraintAnnotationInvocationHandler(annotationType, memberValues));
    }

    /** Constraint annotation invocation handler. */
    @RequiredArgsConstructor
    private static class ConstraintAnnotationInvocationHandler implements InvocationHandler {

        /** Constraint type. */
        private final Class<? extends Annotation> annotationType;
        /** Constraint attributes values. */
        private final LinkedHashMap<String, Object> memberValues;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Modifier.isStatic(method.getModifiers())) { return method.invoke(null, args); }
            String methodName = method.getName();
            switch (methodName) {
                case "hashCode":
                    return hashCode();
                case "toString":
                    return toString();
                case "annotationType":
                    return annotationType;
                case "equals":
                    return equals(args[0]);
                default:
                    return memberValues.get(methodName);
            }
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append('@').append(annotationType.getName());
            result.append('(');
            String attributes = memberValues.entrySet().stream()
                    .map(e -> e.getKey() + '=' + e.getValue())
                    .collect(Collectors.joining(", "));
            result.append(attributes);
            result.append(')');
            return result.toString();
        }

    }

}
