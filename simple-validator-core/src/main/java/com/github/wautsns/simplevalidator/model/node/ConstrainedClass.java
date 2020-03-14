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
import com.github.wautsns.simplevalidator.util.ConstraintUtils;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Constrained class node.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Getter
public class ConstrainedClass extends ConstrainedNode {

    private final List<Annotation> constraints;
    private final List<ConstrainedField> fields;
    private final List<ConstrainedGetter> getters;

    /**
     * The {@code ConstrainedClass} is root node.
     *
     * @return {@code null}
     */
    @Override
    public ConstrainedNode getParent() {
        return null;
    }

    /**
     * Get field nodes and getter nodes.
     *
     * @return field nodes and getter nodes
     * @see #getFields()
     * @see #getGetters()
     */
    @Override
    public List<? extends ConstrainedNode> getChildren() {
        LinkedList<ConstrainedNode> children = new LinkedList<>();
        children.addAll(fields);
        children.addAll(getters);
        return children;
    }

    /**
     * Get field node.
     *
     * @param name name
     * @return field node, or {@code null} if no field is named the specific name
     * @see ConstrainedField#getName(Field)
     */
    public ConstrainedField getField(String name) {
        for (ConstrainedField field : fields) {
            if (field.getLocation().getSimpleName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Get getter node.
     *
     * @param name name
     * @return getter node, or {@code null} if no getter is named the specific name
     * @see ConstrainedGetter#getName(Method)
     */
    public ConstrainedGetter getGetter(String name) {
        for (ConstrainedGetter getter : getters) {
            if (getter.getLocation().getSimpleName().equals(name)) {
                return getter;
            }
        }
        return null;
    }

    /**
     * The {@code ConstrainedClass} is root node.
     *
     * @return {@code null}
     */
    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return null;
    }

    public ConstrainedClass(Class<?> clazz) {
        super(clazz, new Location(clazz.getName()));
        this.constraints = ConstraintUtils.filterOutConstraints(clazz.getAnnotations());
        this.fields = initFields(this);
        this.getters = initGetters(this);
    }

    // -------------------- utils -------------------------------------------------------

    /** instances of constrained class */
    @SuppressWarnings("rawtypes")
    private static final Map<Class, ConstrainedClass> INSTANCES = new ConcurrentHashMap<>();

    /**
     * Get instance associated with the class.
     *
     * @param clazz class
     * @return instance associated with the class
     */
    public static ConstrainedClass getInstance(Class<?> clazz) {
        return INSTANCES.computeIfAbsent(clazz, ConstrainedClass::new);
    }

    /**
     * Resolve field nodes.
     *
     * @param clazz declaring class of the field nodes
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
     * Initialize getter nodes.
     *
     * @param clazz declaring class of the getter nodes
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
