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
     * Require class.
     *
     * @param name class name
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
     * @param declaringClass declaring class of the field
     * @param name field name
     * @return accessible field
     */
    @SneakyThrows
    public static Field requireDeclaredField(Class<?> declaringClass, String name) {
        Field field = declaringClass.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    /**
     * Get the declared field.
     *
     * @param declaringClass declaring class of field
     * @param name field name
     * @return accessible field, or {@code null} if there is no field of the specified name in the specified class
     */
    public static Field getDeclaredField(Class<?> declaringClass, String name) {
        try {
            Field field = declaringClass.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * List declared property fields and set accessible to {@code true}.
     *
     * @param declaringClass declaring class of fields
     * @return accessible property fields
     */
    public static List<Field> listDeclaredPropertyFields(Class<?> declaringClass) {
        List<Field> fields = Arrays.stream(declaringClass.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toCollection(LinkedList::new));
        fields.forEach(field -> field.setAccessible(true));
        return fields;
    }

    /**
     * Get value of field from source.
     *
     * @param source source
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
     * Get {@code boolean} value of field from source.
     *
     * @param source source
     * @param field field
     * @return {@code boolean} value
     */
    @SneakyThrows
    public static boolean getBoolean(Object source, Field field) {
        return field.getBoolean(source);
    }

    /**
     * Get {@code char} value of field from source.
     *
     * @param source source
     * @param field field
     * @return {@code char} value
     */
    @SneakyThrows
    public static char getChar(Object source, Field field) {
        return field.getChar(source);
    }

    /**
     * Get {@code byte} value of field from source.
     *
     * @param source source
     * @param field field
     * @return {@code byte} value
     */
    @SneakyThrows
    public static byte getByte(Object source, Field field) {
        return field.getByte(source);
    }

    /**
     * Get {@code short} value of field from source.
     *
     * @param source source
     * @param field field
     * @return {@code short} value
     */
    @SneakyThrows
    public static short getShort(Object source, Field field) {
        return field.getShort(source);
    }

    /**
     * Get {@code int} value of field from source.
     *
     * @param source source
     * @param field field
     * @return {@code int} value
     */
    @SneakyThrows
    public static int getInt(Object source, Field field) {
        return field.getInt(source);
    }

    /**
     * Get {@code long} value of field from source.
     *
     * @param source source
     * @param field field
     * @return {@code long} value
     */
    @SneakyThrows
    public static long getLong(Object source, Field field) {
        return field.getLong(source);
    }

    /**
     * Get {@code float} value of field from source.
     *
     * @param source source
     * @param field field
     * @return {@code float} value
     */
    @SneakyThrows
    public static float getFloat(Object source, Field field) {
        return field.getFloat(source);
    }

    /**
     * Get {@code double} value of field from source.
     *
     * @param source source
     * @param field field
     * @return {@code double} value
     */
    @SneakyThrows
    public static double getDouble(Object source, Field field) {
        return field.getDouble(source);
    }

    // #################### methods #####################################################

    /**
     * Require the declared method and set accessible to {@code true}.
     *
     * @param declaringClass declaring class of method
     * @param name method name
     * @return accessible method
     */
    @SneakyThrows
    public static Method requireDeclaredMethod(Class<?> declaringClass, String name) {
        Method method = declaringClass.getDeclaredMethod(name, EMPTY_CLASS_ARRAY);
        method.setAccessible(true);
        return method;
    }

    /**
     * Get the declared method and set accessible to {@code true}.
     *
     * @param declaringClass declaring class of method
     * @param name method name
     * @return accessible method, or {@code null} if there is no method of the specified name in the specified class
     */
    public static Method getDeclaredMethod(Class<?> declaringClass, String name) {
        try {
            Method method = declaringClass.getDeclaredMethod(name, EMPTY_CLASS_ARRAY);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Invoke the no-arg method.
     *
     * @param source source
     * @param method method
     * @param <T> type of return value of the method
     * @return return value of the method
     */
    public static <T> T invoke(Object source, Method method) {
        return invoke(source, method, EMPTY_OBJECT_ARRAY);
    }

    /**
     * Invoke the method with specified args.
     *
     * @param source source
     * @param method method
     * @param args method arguments
     * @param <T> type of return value of the method
     * @return return value of the method
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object source, Method method, Object... args) {
        return (T) method.invoke(source, args);
    }

    /**
     * Get the property name corresponding to the method.
     *
     * @param method method
     * @return the property name corresponding to the method, or {@code null} if the method is not a getter/setter
     */
    public static String getPropertyName(Method method) {
        String[] prefixes;
        int parameterCount = method.getParameterCount();
        Class<?> returnType = method.getReturnType();
        if (parameterCount == 0) {
            if (returnType == void.class) {
                return null;
            } else {
                prefixes = GETTER_PREFIXES;
            }
        } else if (parameterCount == 1 && returnType != void.class) {
            prefixes = SETTER_PREFIXES;
        } else {
            return null;
        }
        String name = method.getName();
        int nameLength = name.length();
        for (String prefix : prefixes) {
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
     * @param declaredClass declared class of getters
     * @return declared property getters
     */
    public static List<Method> listDeclaredPropertyGetters(Class<?> declaredClass) {
        return Arrays.stream(declaredClass.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers())
                        && !Modifier.isStatic(method.getModifiers())
                        && getPropertyName(method) != null)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /** Getter prefixes. */
    private static final String[] GETTER_PREFIXES = { "get", "is" };
    /** Setter prefixes. */
    private static final String[] SETTER_PREFIXES = { "set" };

    // #################### constructor #################################################

    /**
     * New instance with no-arg constructor.
     *
     * @param type type
     * @param <T> type of instance
     * @return instance
     */
    @SneakyThrows
    public static <T> T newInstance(Class<T> type) {
        Constructor<T> constructor = type.getConstructor(EMPTY_CLASS_ARRAY);
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    // #################### internal utils ##############################################

    /** Empty class array. */
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    /** Empty object array. */
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

}
