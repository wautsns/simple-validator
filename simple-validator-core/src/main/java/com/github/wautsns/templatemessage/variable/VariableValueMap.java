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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Variable value map.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class VariableValueMap {

    /** Empty variableValueMap. */
    public static final VariableValueMap EMPTY = new VariableValueMap(Collections.emptyMap());

    /** Map: variable -> value. */
    private final Map<Variable, Object> dataMap;

    /** Construct a variableValueMap. */
    public VariableValueMap() {
        this(8);
    }

    /**
     * Construct a variableValueMap.
     *
     * @param initialCapacity initial capacity
     */
    public VariableValueMap(int initialCapacity) {
        dataMap = new HashMap<>(initialCapacity);
    }

    /**
     * Construct a variableValueMap.
     *
     * @param initialVariableValueMap initial variable value map data
     */
    public VariableValueMap(VariableValueMap initialVariableValueMap) {
        this.dataMap = new HashMap<>(initialVariableValueMap.dataMap);
    }

    /**
     * Construct a variableValueMap.
     *
     * @param initialDataMap initial data map
     */
    public VariableValueMap(Map<Variable, Object> initialDataMap) {
        this.dataMap = initialDataMap;
    }

    /**
     * Return whether the variableValueMap is empty.
     *
     * @return {@code true} if the variableValueMap is empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    /**
     * Return whether the variableValueMap contains the specified variable.
     *
     * @param variable variable
     * @return {@code true} if the variableValueMap contains the variable, otherwise {@code false}
     */
    public boolean contains(Variable<?> variable) {
        return dataMap.containsKey(variable);
    }

    /**
     * Return size of the variableValueMap.
     *
     * @return size of the variableValueMap
     */
    public int size() {
        return dataMap.size();
    }

    /**
     * Get variable with the specified name.
     *
     * @param name name
     * @param <T> type of value of variable
     * @return variable with the specified name, or {@code null} if the variableValueMap contains no variable with the
     * specified name
     */
    public <T> Variable<T> getVariable(String name) {
        return dataMap.entrySet().stream()
                .filter(e -> e.getKey().getName().equals(name))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get value of the specified variable.
     *
     * @param variable variable
     * @param <T> type of value
     * @return value of the specified variable, or {@code null} if the variable does not exist in the variable value
     * map, or the value of variable is {@code null}
     */
    public <T> T getValue(Variable<T> variable) {
        return getValue(variable, null);
    }

    /**
     * Get value of the specified variable.
     *
     * @param variable variable
     * @param defaultValue default value
     * @param <T> type of value
     * @return value of the specified variable, or {@code null} if the value of variable is {@code null}, or default
     * value if the variable does not exist in the variable value
     */
    public <T> T getValue(Variable<T> variable, T defaultValue) {
        return (T) dataMap.getOrDefault(variable, defaultValue);
    }

    /**
     * Get dataMap.
     *
     * @return dataMap
     */
    public Map<Variable, Object> getDataMap() {
        return dataMap;
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
        dataMap.put(variable, value);
        return this;
    }

    /**
     * Copy all variables and values from the specified variableValueMap to this variableValueMap.
     *
     * @param variableValueMap another variableValueMap
     * @return self reference
     */
    public VariableValueMap put(VariableValueMap variableValueMap) {
        dataMap.putAll(variableValueMap.dataMap);
        return this;
    }

    /**
     * Remove value of the specified variable.
     *
     * @param variable variable
     * @return self reference
     */
    public VariableValueMap remove(Variable<?> variable) {
        dataMap.remove(variable);
        return this;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('{');
        result.append(dataMap.entrySet().stream()
                .map(entry -> {
                    Variable variable = entry.getKey();
                    Object value = entry.getValue();
                    return variable.getName() + "=" + variable.getFormatter().format(value);
                })
                .collect(Collectors.joining(", ")));
        result.append('}');
        return result.toString();
    }

    @Override
    public int hashCode() {
        return dataMap.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        VariableValueMap that = (VariableValueMap) o;
        return this.dataMap.equals(that.dataMap);
    }

}
