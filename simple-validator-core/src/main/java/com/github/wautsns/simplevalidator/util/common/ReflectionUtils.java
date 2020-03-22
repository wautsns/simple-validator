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

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reflection utils.
 *
 * @author wautsns
 * @since Mar 12, 2020
 */
@UtilityClass
public class ReflectionUtils {

    // #################### class #######################################################

    /**
     * Require class silently.
     *
     * @param name name of the class
     * @return class
     */
    @SneakyThrows
    public static Class<?> requireClass(String name) {
        return Class.forName(name);
    }

    // #################### field #######################################################

    /**
     * Require the declared field and set accessible to {@code true}.
     *
     * @param clazz declaring class of field
     * @param name name of field
     * @return accessible field
     */
    @SneakyThrows
    public static Field requireDeclaredField(Class<?> clazz, String name) {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    /**
     * Get the declared field.
     *
     * @param clazz declaring class of field
     * @param name name of field
     * @return accessible field, or {@code null} if field named the given name is not declared
     */
    public static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * List declared property fields and set accessible to {@code true}.
     *
     * @param clazz declaring class of fields
     * @return accessible property fields
     */
    public static List<Field> listDeclaredPropertyFields(Class<?> clazz) {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toCollection(LinkedList::new));
        fields.forEach(field -> field.setAccessible(true));
        return fields;
    }

    /**
     * Get value of field of source object.
     *
     * @param source source object
     * @param field field
     * @param <T> type of value
     * @return value
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object source, Field field) {
        return (T) field.get(source);
    }

    /**
     * Get {@code boolean} value of field of source object.
     *
     * @param source source object
     * @param field field
     * @return {@code boolean} value
     */
    @SneakyThrows
    public static boolean getBoolean(Object source, Field field) {
        return field.getBoolean(source);
    }

    /**
     * Get {@code char} value of field of source object.
     *
     * @param source source object
     * @param field field
     * @return {@code char} value
     */
    @SneakyThrows
    public static char getChar(Object source, Field field) {
        return field.getChar(source);
    }

    /**
     * Get {@code byte} value of field of source object.
     *
     * @param source source object
     * @param field field
     * @return {@code byte} value
     */
    @SneakyThrows
    public static byte getByte(Object source, Field field) {
        return field.getByte(source);
    }

    /**
     * Get {@code short} value of field of source object.
     *
     * @param source source object
     * @param field field
     * @return {@code short} value
     */
    @SneakyThrows
    public static short getShort(Object source, Field field) {
        return field.getShort(source);
    }

    /**
     * Get {@code int} value of field of source object.
     *
     * @param source source object
     * @param field field
     * @return {@code int} value
     */
    @SneakyThrows
    public static int getInt(Object source, Field field) {
        return field.getInt(source);
    }

    /**
     * Get {@code long} value of field of source object.
     *
     * @param source source object
     * @param field field
     * @return {@code long} value
     */
    @SneakyThrows
    public static long getLong(Object source, Field field) {
        return field.getLong(source);
    }

    /**
     * Get {@code float} value of field of source object.
     *
     * @param source source object
     * @param field field
     * @return {@code float} value
     */
    @SneakyThrows
    public static float getFloat(Object source, Field field) {
        return field.getFloat(source);
    }

    /**
     * Get {@code double} value of field of source object.
     *
     * @param source source object
     * @param field field
     * @return {@code double} value
     */
    @SneakyThrows
    public static double getDouble(Object source, Field field) {
        return field.getDouble(source);
    }

    // -------------------- methods -----------------------------------------------------

    /**
     * Require the declared method and set accessible to {@code true}.
     *
     * @param clazz declaring class of method
     * @param name name of method
     * @return accessible method
     */
    @SneakyThrows
    public static Method requireDeclaredMethod(Class<?> clazz, String name) {
        Method method = clazz.getDeclaredMethod(name, EMPTY_CLASS_ARRAY);
        method.setAccessible(true);
        return method;
    }

    /**
     * Get the declared method and set accessible to {@code true}.
     *
     * @param clazz declaring class of method
     * @param name name of method
     * @return accessible method, or {@code null} if method named the given name is not declared
     */
    public static Method getDeclaredMethod(Class<?> clazz, String name) {
        try {
            Method method = clazz.getDeclaredMethod(name, EMPTY_CLASS_ARRAY);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Invoke the method with no-arg.
     *
     * @param source source object
     * @param method method
     * @param <T> type of return value
     * @return return value of the method
     */
    public static <T> T invoke(Object source, Method method) {
        return invoke(source, method, EMPTY_OBJECT_ARRAY);
    }

    /**
     * Invoke the method with given args.
     *
     * @param source source object
     * @param method method
     * @param args method arguments
     * @param <T> type of return value
     * @return return value of the method
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object source, Method method, Object... args) {
        return (T) method.invoke(source, args);
    }

    /** getter prefixes */
    private static final String[] GETTER_PREFIXES = {"get", "is"};

    /**
     * Get property name of the method.
     *
     * @param method method
     * @return property name of the method, or {@code null} if the method is not a getter
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
     * List declared property getters.
     *
     * @param clazz declared class of getters
     * @return declared property getters
     */
    public static List<Method> listDeclaredPropertyGetters(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers())
                        && !Modifier.isStatic(method.getModifiers())
                        && getPropertyName(method) != null)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    // ------------------------- constructor ---------------------------------------

    /**
     * New instance with no-arg constructor.
     *
     * @param clazz class
     * @param <T> type of class
     * @return instance of the class
     */
    @SneakyThrows
    public static <T> T newInstance(Class<T> clazz) {
        Constructor<T> constructor = clazz.getConstructor(EMPTY_CLASS_ARRAY);
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    // #################### internal utils ##############################################

    /** empty class array */
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    /** empty object array */
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

}
