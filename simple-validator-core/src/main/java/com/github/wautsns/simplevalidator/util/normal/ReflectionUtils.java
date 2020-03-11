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
package com.github.wautsns.simplevalidator.util.normal;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Reflection utils.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@UtilityClass
public class ReflectionUtils {

    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    // -------------------- class -----------------------------------

    /**
     * Get class.
     *
     * @param name name of the class
     * @return class with the specific name
     */
    @SneakyThrows
    public static Class<?> getClass(String name) {
        return Class.forName(name);
    }

    // -------------------- field -----------------------------------

    /**
     * Get field and set accessible to {@code true}.
     *
     * @param clazz declared class of the field
     * @param name name of the field
     * @return accessible field, or {@code null} if the field does not exist
     */
    public static Field accessField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * For each property field.
     *
     * @param clazz target class
     * @param action action for property field
     */
    public static void forEachPropertyField(Class<?> clazz, Consumer<Field> action) {
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .forEach(action);
    }

    /**
     * Get field value.
     *
     * @param source source object
     * @param field field
     * @return value
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object source, Field field) {
        return (T) field.get(source);
    }

    /**
     * Get field boolean.
     *
     * @param source source object
     * @param field field
     * @return {@code boolean} value
     */
    @SneakyThrows
    public static boolean getFieldBoolean(Object source, Field field) {
        return field.getBoolean(source);
    }

    /**
     * Get field char.
     *
     * @param source source object
     * @param field field
     * @return {@code char} value
     */
    @SneakyThrows
    public static char getFieldChar(Object source, Field field) {
        return field.getChar(source);
    }

    /**
     * Get field byte.
     *
     * @param source source object
     * @param field field
     * @return {@code byte} value
     */
    @SneakyThrows
    public static byte getFieldByte(Object source, Field field) {
        return field.getByte(source);
    }

    /**
     * Get field short.
     *
     * @param source source object
     * @param field field
     * @return {@code short} value
     */
    @SneakyThrows
    public static short getFieldShort(Object source, Field field) {
        return field.getShort(source);
    }

    /**
     * Get field int.
     *
     * @param source source object
     * @param field field
     * @return {@code int} value
     */
    @SneakyThrows
    public static int getFieldInt(Object source, Field field) {
        return field.getInt(source);
    }

    /**
     * Get field long.
     *
     * @param source source object
     * @param field field
     * @return {@code long} value
     */
    @SneakyThrows
    public static long getFieldLong(Object source, Field field) {
        return field.getLong(source);
    }

    /**
     * Get field float.
     *
     * @param source source object
     * @param field field
     * @return {@code float} value
     */
    @SneakyThrows
    public static float getFieldFloat(Object source, Field field) {
        return field.getFloat(source);
    }

    /**
     * Get field double.
     *
     * @param source source object
     * @param field field
     * @return {@code double} value
     */
    @SneakyThrows
    public static double getFieldDouble(Object source, Field field) {
        return field.getDouble(source);
    }

    // -------------------- methods ---------------------------------

    /**
     * Get method and set accessible to {@code true}.
     *
     * @param clazz declared class of the method
     * @param name name of the method
     * @return accessible method, or {@code null} if the method does not exist
     */
    public static Method accessMethod(Class<?> clazz, String name) {
        try {
            Method method = clazz.getDeclaredMethod(name, EMPTY_CLASS_ARRAY);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Invoke method with no-arg.
     *
     * @param source source object
     * @param method method
     * @return value of the method
     */
    public static <T> T invoke(Object source, Method method) {
        return invoke(source, method, EMPTY_OBJECT_ARRAY);
    }

    /**
     * Invoke method.
     *
     * @param source source object
     * @param method method
     * @param args method arguments
     * @return value of the method
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object source, Method method, Object... args) {
        return (T) method.invoke(source, args);
    }

    /** getter prefixes */
    private static final String[] GETTER_PREFIXES = {"get", "is"};

    /**
     * Get property name of getter.
     *
     * @param method method
     * @return property name of method, or {@code null} if the method is not a getter
     */
    public static String getPropertyName(Method method) {
        if (method.getParameterCount() != 0) { return null; }
        if (method.getReturnType() == void.class) { return null; }
        String name = method.getName();
        int nameLength = name.length();
        for (String prefix : GETTER_PREFIXES) {
            int prefixLength = prefix.length();
            if (name.startsWith(prefix) && (name.length() > prefixLength)) {
                char first = name.charAt(prefixLength);
                if (Character.isUpperCase(first)) {
                    first = Character.toLowerCase(first);
                    int secondIndex = prefixLength + 1;
                    if (nameLength == secondIndex) {
                        return String.valueOf(first);
                    } else {
                        return first + name.substring(secondIndex);
                    }
                }
            }
        }
        return null;
    }

    /**
     * For each property: getter.
     *
     * @param clazz target class
     * @param action action for property name and getter
     */
    public static void forEachPropertyGetter(Class<?> clazz, BiConsumer<String, Method> action) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .forEach(method -> {
                    String correspondingFieldName = getPropertyName(method);
                    if (correspondingFieldName == null) { return; }
                    action.accept(correspondingFieldName, method);
                });
    }

    // ------------------------- constructor ---------------------------------------

    /**
     * New instance by no-arg constructor.
     *
     * @param clazz class
     * @param <T> type of the class
     * @return instance of the class
     */
    @SneakyThrows
    public static <T> T newInstance(Class<T> clazz) {
        Constructor<T> constructor = clazz.getConstructor(EMPTY_CLASS_ARRAY);
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

}
