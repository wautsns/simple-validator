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
package com.github.wautsns.simplevalidator.model.node;

import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.util.common.CollectionUtils;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The constrained class.
 *
 * @author wautsns
 * @since Mar 19, 2020
 */
@Getter
public class ConstrainedClass extends ConstrainedNode {

    /** fields */
    private final List<ConstrainedField> fields;
    /** getters */
    private final List<ConstrainedGetter> getters;

    /**
     * The {@code ConstrainedClass} is <strong>root</strong>.
     *
     * @return {@code null}
     */
    @Override
    public ConstrainedNode getParent() {
        return null;
    }

    /**
     * Get field.
     *
     * @param name field name
     * @return field, or {@code null} if there is no field named the specified name
     * @see ConstrainedField#generateName(Field)
     */
    public ConstrainedField getField(String name) {
        return getNode(fields, name);
    }

    /**
     * Get getter.
     *
     * @param name getter name
     * @return getter, or {@code null} if there is no getter named the specified name
     * @see ConstrainedGetter#generateName(Method)
     */
    public ConstrainedGetter getGetter(String name) {
        return getNode(getters, name);
    }

    @Override
    public List<? extends ConstrainedNode> getChildren() {
        List<? extends ConstrainedNode> superChildren = super.getChildren();
        if (superChildren.isEmpty()) {
            if (getters.isEmpty()) {
                return fields;
            } else if (fields.isEmpty()) {
                return getters;
            }
        }
        List<ConstrainedNode> children = new LinkedList<>();
        children.addAll(superChildren);
        children.addAll(fields);
        children.addAll(getters);
        return CollectionUtils.unmodifiableList(children);
    }

    /**
     * The {@code ConstrainedClass} is <strong>root</strong>.
     *
     * @return {@code null}
     */
    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return null;
    }

    // #################### instance ####################################################

    /** type -> constrained class map */
    private static final Map<Class<?>, ConstrainedClass> INSTANCE_MAP = new ConcurrentHashMap<>(64);

    /**
     * Get {@code ConstrainedNode} instance for the specified class.
     *
     * @param clazz class
     * @return {@code ConstrainedNode} instance for the specified class
     */
    public static ConstrainedClass getInstance(Class<?> clazz) {
        return INSTANCE_MAP.computeIfAbsent(clazz, ConstrainedClass::new);
    }

    // ==================== constructor =================================================

    /**
     * Construct a constrained class.
     *
     * @param clazz class
     */
    private ConstrainedClass(Class<?> clazz) {
        super(clazz.getName(), clazz, clazz.getDeclaredAnnotations());
        this.fields = initFields(this);
        this.getters = initGetters(this);
    }

    // #################### internal utils ##############################################

    /**
     * Initialize fields.
     *
     * @param clazz declaring class of fields
     * @return constrained field nodes
     */
    private static List<ConstrainedField> initFields(ConstrainedClass clazz) {
        List<Field> fields = ReflectionUtils.listDeclaredPropertyFields((Class<?>) clazz.getType());
        List<ConstrainedField> nodes = fields.stream()
                .map(field -> new ConstrainedField(clazz, field))
                .collect(Collectors.toCollection(LinkedList::new));
        return clear(nodes);
    }

    /**
     * Initialize getters.
     *
     * @param clazz declaring class of getters
     * @return constrained getter nodes
     */
    private static List<ConstrainedGetter> initGetters(ConstrainedClass clazz) {
        List<Method> getters = ReflectionUtils.listDeclaredPropertyGetters((Class<?>) clazz.getType());
        List<ConstrainedGetter> nodes = getters.stream()
                .map(getter -> new ConstrainedGetter(clazz, getter))
                .collect(Collectors.toCollection(LinkedList::new));
        return clear(nodes);
    }

}
