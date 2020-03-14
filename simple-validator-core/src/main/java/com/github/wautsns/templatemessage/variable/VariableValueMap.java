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
package com.github.wautsns.templatemessage.variable;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Variable value map.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@EqualsAndHashCode
public class VariableValueMap implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 155772768719651061L;

    /** serializable data */
    private Map<Variable<?>, Serializable> serializableData = Collections.emptyMap();
    /** unserializable data */
    private transient Map<Variable<?>, Object> unserializableData = Collections.emptyMap();

    /**
     * Put variable value map.
     *
     * @param another another variable value map
     * @return self reference
     */
    public VariableValueMap put(VariableValueMap another) {
        another.forEach(this::put);
        return this;
    }

    /**
     * Put variable and value.
     *
     * @param variable variable
     * @param value value
     * @param <T> type of value
     * @return self reference
     */
    public <T> VariableValueMap put(Variable<T> variable, T value) {
        if (value == null || value instanceof Serializable) {
            if (serializableData.isEmpty()) { serializableData = new HashMap<>(8, 1f); }
            unserializableData.remove(variable);
            serializableData.put(variable, (Serializable) value);
        } else {
            if (unserializableData.isEmpty()) { unserializableData = new HashMap<>(4, 1f); }
            serializableData.remove(variable);
            unserializableData.put(variable, value);
        }
        return this;
    }

    /** Clear the map. */
    public void clear() {
        serializableData.clear();
        unserializableData.clear();
    }

    /**
     * Remove the value associated with the variable
     *
     * @param variable variable
     * @return self reference
     */
    public VariableValueMap remove(Variable<?> variable) {
        serializableData.remove(variable);
        unserializableData.remove(variable);
        return this;
    }

    /**
     * Remove and return the value associated with the variable.
     *
     * @param variable variable
     * @param <T> type of value
     * @return value associated with the variable, or {@code null} if the map contains no mapping for the variable
     */
    public <T> T removeAndGet(Variable<T> variable) {
        return removeAndGet(variable, null);
    }

    /**
     * Remove and return the value associated with the variable.
     *
     * @param variable variable
     * @param defaultValue default value
     * @param <T> type of value
     * @return value associated with the variable, or {@code defaultValue} if the map contains no mapping for the
     * variable
     */
    @SuppressWarnings("unchecked")
    public <T> T removeAndGet(Variable<T> variable, T defaultValue) {
        T value = (T) serializableData.remove(variable);
        if (value != null) { return value; }
        if (serializableData.containsKey(variable)) { return null; }
        value = (T) unserializableData.remove(variable);
        if (value != null) { return value; }
        if (unserializableData.containsKey(variable)) { return null; }
        return defaultValue;
    }

    /**
     * Return {@code true} if the map is empty, otherwise {@code false}.
     *
     * @return {@code true} if the map is empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return serializableData.isEmpty() && unserializableData.isEmpty();
    }

    /**
     * Return {@code true} if the map contains the variable, otherwise {@code false}.
     *
     * @param variable variable
     * @return {@code true} if the map contains the variable, otherwise {@code false}
     */
    public boolean containsVariable(Variable<?> variable) {
        return serializableData.containsKey(variable) || unserializableData.containsKey(variable);
    }

    /**
     * Return size of the map.
     *
     * @return size of the map
     */
    public int size() {
        return serializableData.size() + unserializableData.size();
    }

    /**
     * Get the variable associated with the name.
     *
     * @param name name
     * @param <T> type of value of variable
     * @return variable associated with the name, or {@code null} if the map contains no variable with specific name.
     */
    @SuppressWarnings("unchecked")
    public <T> Variable<T> getVariable(String name) {
        return entryStream()
                .filter(e -> e.getKey().getName().equals(name))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get the value associated with the variable.
     *
     * @param variable variable
     * @param <T> type of value
     * @return value associated with the variable
     */
    public <T> T getValue(Variable<T> variable) {
        return getValue(variable, null);
    }

    /**
     * Get the value associated with the variable.
     *
     * @param variable variable
     * @param defaultValue default value
     * @param <T> type of value
     * @return value associated with the variable, or {@code defaultValue} if the map contains no mapping for the
     * variable
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(Variable<T> variable, T defaultValue) {
        T value = (T) serializableData.get(variable);
        if (value != null) { return value; }
        if (serializableData.containsKey(variable)) { return null; }
        value = (T) unserializableData.get(variable);
        if (value != null) { return value; }
        if (unserializableData.containsKey(variable)) { return null; }
        return defaultValue;
    }

    /**
     * Return entry stream of the map.
     *
     * @return entry stream of the map
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Stream<Map.Entry<Variable, Object>> entryStream() {
        if (serializableData.isEmpty()) {
            if (unserializableData.isEmpty()) {
                return Stream.empty();
            } else {
                return (Stream) unserializableData.entrySet().stream();
            }
        } else if (unserializableData.isEmpty()) {
            return (Stream) serializableData.entrySet().stream();
        } else {
            return Stream.concat(
                    (Stream) serializableData.entrySet().stream(),
                    unserializableData.entrySet().stream());
        }
    }

    /**
     * For each variable and value.
     *
     * @param action action for variable and value
     */
    @SuppressWarnings("rawtypes")
    public void forEach(BiConsumer<Variable, Object> action) {
        serializableData.forEach(action);
        unserializableData.forEach(action);
    }

    /**
     * Return string format of the map.
     *
     * @return string format of the map
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('{');
        String inner = entryStream()
                .map(e -> {
                    Variable variable = e.getKey();
                    Object value = e.getValue();
                    return variable.getName() + '=' + variable.getFormatter().format(value, Locale.getDefault());
                })
                .collect(Collectors.joining(", "));
        result.append(inner);
        result.append('}');
        return result.toString();
    }

    /** empty variable value map */
    public static final VariableValueMap EMPTY = new VariableValueMap() {

        /** serialVersionUID */
        private static final long serialVersionUID = -4503979970242041809L;

        @Override
        public <T> VariableValueMap put(Variable<T> variable, T value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T removeAndGet(Variable<T> variable, T defaultValue) {
            return defaultValue;
        }

        @Override
        public <T> T getValue(Variable<T> variable, T defaultValue) {
            return defaultValue;
        }

        @Override
        @SuppressWarnings("rawtypes")
        public Stream<Map.Entry<Variable, Object>> entryStream() {
            return Stream.empty();
        }

    };

}
