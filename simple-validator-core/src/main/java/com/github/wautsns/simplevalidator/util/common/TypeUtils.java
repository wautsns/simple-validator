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
     * Get class of the specified type.
     *
     * <pre>
     * // class
     * Integer.class &lt;== Integer
     * Long[].class &lt;== Long[]
     * // generic array type
     * List[].class &lt;== List&lt;String&gt;[]
     * // parameterized type
     * HashMap.class &lt;== HashMap&lt;String, Object&gt;
     * // type variable
     * Number.class &lt;== T extends Number & Comparable&lt;T&gt;
     * // wildcard type
     * Number.class &lt;== ? extends Number.
     * Number.class &lt;== ? super Number.
     * Number.class &lt;== ? super T, T extends Number & Comparable&lt;T&gt;.
     * </pre>
     *
     * @param type type
     * @return class of the specified type
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
     * Return whether the specified type is primitive.
     *
     * @param type type
     * @return {@code true} if the specified type is primitive, otherwise {@code false}
     */
    public static boolean isPrimitive(Type type) {
        return (type instanceof Class && ((Class<?>) type).isPrimitive());
    }

    /**
     * Return whether the specified type is array.
     *
     * @param type type
     * @return {@code true} if the specified type is array, otherwise {@code false}
     */
    public static boolean isArray(Type type) {
        return (getComponentType(type) != null);
    }

    /**
     * Return whether the specified type is enum.
     *
     * @param type type
     * @return {@code true} if the specified type is enum, otherwise {@code false}
     */
    public static boolean isEnum(Type type) {
        return getClass(type).isEnum();
    }

    /**
     * Get component type of the specified type.
     *
     * @param type type
     * @return component type of the specified type, or {@code null} if the specified type is not array
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

    // #################### is assignable to ############################################

    /**
     * Return whether the specified type is assignable to the specified class.
     *
     * @param type type
     * @param clazz class
     * @return {@code true} if the specified type is assignable to the specified class, otherwise {@code false}
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
     * Return whether the specified type is assignable to any of the specified classes.
     *
     * @param type type
     * @param classes classes
     * @return {@code true} if the specified type is assignable to any of the specified classes, otherwise {@code false}
     */
    public static boolean isAssignableToAny(Type type, Class<?>... classes) {
        return Arrays.stream(classes).anyMatch(clazz -> isAssignableTo(type, clazz));
    }

    /**
     * Return whether the specified type is assignable to all specified classes.
     *
     * @param type type
     * @param classes classes
     * @return {@code true} if the specified type is assignable to all specified classes, otherwise {@code false}
     */
    public static boolean isAssignableToAll(Type type, Class<?>... classes) {
        return Arrays.stream(classes).allMatch(clazz -> isAssignableTo(type, clazz));
    }

    /**
     * Return whether the specified type is assignable to any of the specified classes.
     *
     * @param type type
     * @param classes classes
     * @return {@code true} if the specified type is assignable to any of the specified classes, otherwise {@code false}
     */
    public static boolean isAssignableToAny(Type type, Iterable<Class<?>> classes) {
        return StreamSupport.stream(classes.spliterator(), false).anyMatch(clazz -> isAssignableTo(type, clazz));
    }

    /**
     * Return whether the specified type is assignable to all specified classes.
     *
     * @param type type
     * @param classes classes
     * @return {@code true} if the specified type is assignable to all specified classes, otherwise {@code false}
     */
    public static boolean isAssignableToAll(Type type, Iterable<Class<?>> classes) {
        return StreamSupport.stream(classes.spliterator(), false).allMatch(clazz -> isAssignableTo(type, clazz));
    }

    // #################### type parameter ##############################################

    /**
     * Get type parameter index corresponding to the target type parameter index.
     *
     * <pre>
     * // example A:
     * public class MyMapA&lt;K, V&gt; extends HashMap&lt;K, V&gt; {}
     * -1 &lt;== getTypeParameterIndex(MyMap.class, LinkedList.class, 0);
     * 0 &lt;== getTypeParameterIndex(MyMap.class, Map.class, 0);
     * 1 &lt;== getTypeParameterIndex(MyMap.class, Map.class, 1);
     * // example B:
     * public class MyMapB&lt;V&gt; extends HashMap&lt;String, V&gt; {}
     * -1 &lt;== getTypeParameterIndex(MyMap.class, Map.class, 0);
     * 0 &lt;== getTypeParameterIndex(MyMap.class, Map.class, 1);
     * 0 &lt;== getTypeParameterIndex(MyMap.class, HashMap.class, 1);
     * </pre>
     *
     * @param type type
     * @param targetType target type
     * @param targetTypeParameterIndex target type parameter index
     * @return type parameter index, or {@code -1} if there is no correspondence
     */
    public static int getTypeParameterIndex(Class<?> type, Class<?> targetType, int targetTypeParameterIndex) {
        if (!targetType.isAssignableFrom(type)) {
            return -1;
        } else if (targetType.isInterface()) {
            return getTypeParameterIndexForInterface(type, targetType, targetTypeParameterIndex);
        } else {
            return getTypeParameterIndexForSuperclass(type, targetType, targetTypeParameterIndex);
        }
    }

    /**
     * Get type parameter index corresponding to the target interface parameter index.
     *
     * <p>The type must be assignable to target type.
     *
     * @param type type
     * @param targetType target interface
     * @param targetTypeParameterIndex target type parameter index
     * @return type parameter index, or {@code -1} if there is no correspondence
     */
    private static int getTypeParameterIndexForInterface(
            Class<?> type, Class<?> targetType, int targetTypeParameterIndex) {
        int typeParametersLength = type.getTypeParameters().length;
        for (int typeParameterIndex = 0; typeParameterIndex < typeParametersLength; typeParameterIndex++) {
            if (checkTypeParameterIndexForInterface(type, typeParameterIndex, targetType, targetTypeParameterIndex)) {
                return typeParameterIndex;
            }
        }
        return -1;
    }

    /**
     * Get type parameter index corresponding to the target interface parameter index.
     *
     * <p>The type must be assignable to target type.
     *
     * @param type type
     * @param targetType target superclass
     * @param targetTypeParameterIndex target type parameter index
     * @return type parameter index, or {@code -1} if there is no correspondence
     */
    private static int getTypeParameterIndexForSuperclass(
            Class<?> type, Class<?> targetType, int targetTypeParameterIndex) {
        int typeParametersLength = type.getTypeParameters().length;
        for (int typeParameterIndex = 0; typeParameterIndex < typeParametersLength; typeParameterIndex++) {
            if (checkTypeParameterForSuperclass(type, typeParameterIndex, targetType, targetTypeParameterIndex)) {
                return typeParameterIndex;
            }
        }
        return -1;
    }

    /**
     * Check the specified type parameter index.
     *
     * <p>The type must be assignable to target type.
     *
     * @param type type
     * @param typeParameterIndex type parameter index
     * @param targetType target interface
     * @param targetTypeParameterIndex target type parameter index
     * @return type parameter index, or {@code -1} if there is no correspondence
     */
    private static boolean checkTypeParameterIndexForInterface(
            Class<?> type, int typeParameterIndex, Class<?> targetType, int targetTypeParameterIndex) {
        if (type == targetType) { return typeParameterIndex == targetTypeParameterIndex; }
        TypeVariable<?>[] typeParameters = type.getTypeParameters();
        if (typeParameters.length <= typeParameterIndex) { return false; }
        TypeVariable<?> typeParameter = typeParameters[typeParameterIndex];
        for (Type genericInterface : type.getGenericInterfaces()) {
            if (!(genericInterface instanceof ParameterizedType)) { return false; }
            ParameterizedType parameterizedInterface = (ParameterizedType) genericInterface;
            Class<?> interfaceClass = (Class<?>) parameterizedInterface.getRawType();
            if (!targetType.isAssignableFrom(interfaceClass)) { continue; }
            Type[] actualTypeArguments = parameterizedInterface.getActualTypeArguments();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type actualTypeArgument = actualTypeArguments[i];
                if (actualTypeArgument == typeParameter
                        && checkTypeParameterIndexForInterface(
                        interfaceClass, i, targetType, targetTypeParameterIndex)) {
                    return true;
                }
            }
        }
        Class<?> superclass = type.getSuperclass();
        int index = getTypeParameterIndexForInterface(superclass, targetType, targetTypeParameterIndex);
        return (index != -1) && checkTypeParameterForSuperclass(type, typeParameterIndex, superclass, index);
    }

    /**
     * Check the specified type parameter index.
     *
     * <p>The type must be assignable to target type.
     *
     * @param type type
     * @param typeParameterIndex type parameter index
     * @param targetType target interface
     * @param targetTypeParameterIndex target type parameter index
     * @return type parameter index, or {@code -1} if there is no correspondence
     */
    private static boolean checkTypeParameterForSuperclass(
            Class<?> type, int typeParameterIndex, Class<?> targetType, int targetTypeParameterIndex) {
        if (type == targetType) { return typeParameterIndex == targetTypeParameterIndex; }
        TypeVariable<?>[] typeParameters = type.getTypeParameters();
        if (typeParameters.length <= typeParameterIndex) { return false; }
        TypeVariable<?> typeParameter = typeParameters[typeParameterIndex];
        Type genericSuperclass = type.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) { return false; }
        ParameterizedType parameterizedSuperclass = (ParameterizedType) genericSuperclass;
        Class<?> superclass = (Class<?>) parameterizedSuperclass.getRawType();
        Type[] actualTypeArguments = parameterizedSuperclass.getActualTypeArguments();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            Type actualTypeArgument = actualTypeArguments[i];
            if (actualTypeArgument == typeParameter
                    && checkTypeParameterForSuperclass(superclass, i, targetType, targetTypeParameterIndex)) {
                return true;
            }
        }
        return false;
    }

}
