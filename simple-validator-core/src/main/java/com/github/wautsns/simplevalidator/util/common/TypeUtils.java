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

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.stream.StreamSupport;

/**
 * Type utils.
 *
 * @author wautsns
 * @since Mar 12, 2020
 */
@UtilityClass
public class TypeUtils {

    /**
     * Get class of the type.
     *
     * @param type type
     * @return class of the type
     */
    public static Class<?> getClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            Type genericComponentType = genericArrayType.getGenericComponentType();
            Class<?> componentClass = getClass(genericComponentType);
            return Array.newInstance(componentClass, 0).getClass();
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            return getClass(rawType);
        } else if (type instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            Type[] bounds = typeVariable.getBounds();
            return getClass(bounds[0]);
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] lowerBounds = wildcardType.getLowerBounds();
            if (lowerBounds.length != 0) { return getClass(lowerBounds[0]); }
            Type[] upperBounds = wildcardType.getUpperBounds();
            if (upperBounds.length != 0) { return getClass(upperBounds[0]); }
        }
        throw new IllegalStateException();
    }

    /**
     * Whether the type is primitive.
     *
     * @param type type
     * @return {@code true} if the type is primitive, otherwise {@code false}.
     */
    public static boolean isPrimitive(Type type) {
        return (type instanceof Class && ((Class<?>) type).isPrimitive());
    }

    /**
     * Whether the type is array.
     *
     * @param type type
     * @return {@code true} if the type is array, otherwise {@code false}.
     */
    public static boolean isArray(Type type) {
        return (getComponentType(type) != null);
    }

    /**
     * Whether the type is enum.
     *
     * @param type type
     * @return {@code true} if the type is enum, otherwise {@code false}.
     */
    public static boolean isEnum(Type type) {
        return getClass(type).isEnum();
    }

    /**
     * Get component type of the type.
     *
     * @param type type
     * @return component type of the type, or {@code null} if the type is not array.
     */
    public static Type getComponentType(Type type) {
        if (type instanceof Class) {
            return ((Class<?>) type).getComponentType();
        } else if (type instanceof GenericArrayType) {
            return ((GenericArrayType) type).getGenericComponentType();
        } else {
            return null;
        }
    }

    /**
     * Whether the type is assignable to the class.
     *
     * @param type type
     * @param clazz class
     * @return {@code true} if the type is assignable to the class, otherwise {@code false}
     */
    public static boolean isAssignableTo(Type type, Class<?> clazz) {
        if (type instanceof Class) {
            return clazz.isAssignableFrom((Class<?>) type);
        } else if (type instanceof GenericArrayType) {
            if (!clazz.isArray()) { return false; }
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return isAssignableTo(componentType, clazz.getComponentType());
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            return isAssignableTo(rawType, clazz);
        } else if (type instanceof TypeVariable) {
            Type[] bounds = ((TypeVariable<?>) type).getBounds();
            return Arrays.stream(bounds).anyMatch(bound -> isAssignableTo(bound, clazz));
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            return Arrays.stream(wildcardType.getLowerBounds())
                    .anyMatch(lowerBound -> isAssignableTo(lowerBound, clazz))
                    || Arrays.stream(wildcardType.getUpperBounds())
                    .anyMatch(upperBound -> isAssignableTo(upperBound, clazz));
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Whether the type is assignable to any of the given classes.
     *
     * @param type type
     * @param classes classes
     * @return {@code true} if the type is assignable to any of the given classes, otherwise {@code false}
     */
    public static boolean isAssignableToAny(Type type, Class<?>... classes) {
        return Arrays.stream(classes).anyMatch(clazz -> isAssignableTo(type, clazz));
    }

    /**
     * Whether the type is assignable to all given classes.
     *
     * @param type type
     * @param classes classes
     * @return {@code true} if the type is assignable to all given classes, otherwise {@code false}
     */
    public static boolean isAssignableToAll(Type type, Class<?>... classes) {
        return Arrays.stream(classes).allMatch(clazz -> isAssignableTo(type, clazz));
    }

    /**
     * Whether the type is assignable to any of the given classes.
     *
     * @param type type
     * @param classes classes
     * @return {@code true} if the type is assignable to any of the given classes, otherwise {@code false}
     */
    public static boolean isAssignableToAny(Type type, Iterable<Class<?>> classes) {
        return StreamSupport.stream(classes.spliterator(), false).anyMatch(clazz -> isAssignableTo(type, clazz));
    }

    /**
     * Whether the type is assignable to all given classes.
     *
     * @param type type
     * @param classes classes
     * @return {@code true} if the type is assignable to all given classes, otherwise {@code false}
     */
    public static boolean isAssignableToAll(Type type, Iterable<Class<?>> classes) {
        return StreamSupport.stream(classes.spliterator(), false).allMatch(clazz -> isAssignableTo(type, clazz));
    }

}
