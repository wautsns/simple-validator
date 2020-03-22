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

    private static final long serialVersionUID = 155772768719651061L;

    /** serializable value map */
    private Map<Variable<?>, Serializable> serializableValueMap = Collections.emptyMap();
    /** non-serializable value map */
    private transient Map<Variable<?>, Object> nonSerializableValueMap = Collections.emptyMap();

    /**
     * Whether the variable value map is empty.
     *
     * @return {@code true} if the variable value map is empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return serializableValueMap.isEmpty() && nonSerializableValueMap.isEmpty();
    }

    /**
     * Whether the variable value map contains the variable.
     *
     * @param variable variable
     * @return {@code true} if the variable value map contains the variable, otherwise {@code false}
     */
    public boolean containsVariable(Variable<?> variable) {
        return serializableValueMap.containsKey(variable) || nonSerializableValueMap.containsKey(variable);
    }

    /**
     * Return size of the variable value map.
     *
     * @return size of the variable value map
     */
    public int size() {
        return serializableValueMap.size() + nonSerializableValueMap.size();
    }

    /**
     * Get the variable with the specific name.
     *
     * @param name name
     * @param <T> type of value of variable
     * @return the variable with the specific name, or {@code null} if the variable value map contains no variable with
     * the specific name
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
     * Get the value of the specific variable.
     *
     * @param variable variable
     * @param <T> type of value
     * @return the value of the specific variable, or {@code null} if the variable does not exist in the variable value
     * map, or the value of variable is {@code null}
     */
    public <T> T getValue(Variable<T> variable) {
        return getValue(variable, null);
    }

    /**
     * Get the value of the specific variable.
     *
     * @param variable variable
     * @param defaultValue default value
     * @param <T> type of value
     * @return the value of the specific variable, or {@code null} if the value of variable is {@code null}, or default
     * value if the variable does not exist in the variable value
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(Variable<T> variable, T defaultValue) {
        T value = (T) serializableValueMap.get(variable);
        if (value != null) { return value; }
        if (serializableValueMap.containsKey(variable)) { return null; }
        value = (T) nonSerializableValueMap.get(variable);
        if (value != null) { return value; }
        if (nonSerializableValueMap.containsKey(variable)) { return null; }
        return defaultValue;
    }

    /**
     * Return entry stream of the variable value map.
     *
     * @return entry stream of the variable value map
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Stream<Map.Entry<Variable, Object>> entryStream() {
        if (serializableValueMap.isEmpty()) {
            if (nonSerializableValueMap.isEmpty()) {
                return Stream.empty();
            } else {
                return (Stream) nonSerializableValueMap.entrySet().stream();
            }
        } else {
            Stream serializableDataStream = serializableValueMap.entrySet().stream();
            if (nonSerializableValueMap.isEmpty()) { return serializableDataStream; }
            return Stream.concat(serializableDataStream, nonSerializableValueMap.entrySet().stream());
        }
    }

    /**
     * For each variable and value.
     *
     * @param action action for variable and value
     */
    @SuppressWarnings("rawtypes")
    public void forEach(BiConsumer<Variable, Object> action) {
        serializableValueMap.forEach(action);
        nonSerializableValueMap.forEach(action);
    }

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
            if (serializableValueMap.isEmpty()) { serializableValueMap = new HashMap<>(8, 1f); }
            nonSerializableValueMap.remove(variable);
            serializableValueMap.put(variable, (Serializable) value);
        } else {
            if (nonSerializableValueMap.isEmpty()) { nonSerializableValueMap = new HashMap<>(4, 1f); }
            serializableValueMap.remove(variable);
            nonSerializableValueMap.put(variable, value);
        }
        return this;
    }

    /** Clear the map. */
    public void clear() {
        serializableValueMap.clear();
        nonSerializableValueMap.clear();
    }

    /**
     * Remove the value associated with the variable
     *
     * @param variable variable
     * @return self reference
     */
    public VariableValueMap remove(Variable<?> variable) {
        serializableValueMap.remove(variable);
        nonSerializableValueMap.remove(variable);
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
        T value = (T) serializableValueMap.remove(variable);
        if (value != null) { return value; }
        if (serializableValueMap.containsKey(variable)) { return null; }
        value = (T) nonSerializableValueMap.remove(variable);
        if (value != null) { return value; }
        if (nonSerializableValueMap.containsKey(variable)) { return null; }
        return defaultValue;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('{');
        result.append(entryStream()
                .map(e -> {
                    Variable variable = e.getKey();
                    Object value = e.getValue();
                    return variable + "=" + variable.getFormatter().format(value);
                })
                .collect(Collectors.joining(", ")));
        result.append('}');
        return result.toString();
    }

    /** empty variable value map */
    public static final VariableValueMap EMPTY = new VariableValueMap() {

        /** serialVersionUID */
        private static final long serialVersionUID = -4503979970242041809L;

        @Override
        public <T> T getValue(Variable<T> variable, T defaultValue) {
            return defaultValue;
        }

        @Override
        @SuppressWarnings("rawtypes")
        public Stream<Map.Entry<Variable, Object>> entryStream() {
            return Stream.empty();
        }

        @Override
        public <T> VariableValueMap put(Variable<T> variable, T value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T removeAndGet(Variable<T> variable, T defaultValue) {
            return defaultValue;
        }

    };

}
