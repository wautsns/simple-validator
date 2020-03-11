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

import com.github.wautsns.simplevalidator.util.ConstraintUtils;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Node with constraints.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@Getter
public class ConstrainedNode {

    /**
     * parent node
     *
     * <p>If the node is root, the value is {@code null}.
     */
    private final ConstrainedNode parent;
    /** children of the node(nonnull unmodified list) */
    private final List<ConstrainedNode> children;

    /** category of the node */
    private final Category category;
    /**
     * original element
     *
     * <p>If the category is not in [TYPE, FIELD, GETTER, PARAMETER], the value is {@code null}.
     */
    private final AnnotatedElement origin;
    /** type of the node */
    private final Type type;
    /** name of the node */
    private final String name;
    /** constraints on the node(nonnull unmodified list) on the node */
    private final List<Annotation> constraints;

    /**
     * Get location of the node.
     *
     * @return location of the node
     */
    public List<String> getLocation() {
        List<String> location = (parent == null) ? new LinkedList<>() : parent.getLocation();
        location.add(name);
        return location;
    }

    /**
     * Get child.
     *
     * @param name name of child
     * @return child
     */
    public ConstrainedNode getChild(String name) {
        for (ConstrainedNode child : children) {
            if (child.name.equals(name)) {
                return child;
            }
        }
        throw new IllegalArgumentException();
    }

    public ConstrainedNode(Class<?> clazz) {
        this.category = Category.TYPE;
        this.parent = null;
        this.origin = clazz;
        this.type = clazz;
        this.name = InternalUtils.getElementName(category, origin);
        this.constraints = ConstraintUtils.getConstraints(clazz.getAnnotations());
        this.children = InternalUtils.resolve(this, Collections.emptyMap(), Collections.emptyList());
    }

    public ConstrainedNode(Parameter parameter) {
        this(Category.PARAMETER, null, parameter, parameter.getAnnotatedType());
    }

    public ConstrainedNode(
            Category category,
            ConstrainedNode parent, AnnotatedElement origin, AnnotatedType annotatedType) {
        this(
                category,
                parent, origin, annotatedType.getType(),
                InternalUtils.getIndexesConstraintsMap(annotatedType), Collections.emptyList());
    }

    ConstrainedNode(
            Category category,
            ConstrainedNode parent, AnnotatedElement origin, Type type,
            Map<List<Short>, List<Annotation>> indexesConstraintsMap, List<Short> indexes) {
        this.category = category;
        this.parent = parent;
        this.origin = origin;
        this.type = type;
        this.name = InternalUtils.getElementName(category, origin);
        this.constraints = indexesConstraintsMap.getOrDefault(indexes, Collections.emptyList());
        this.children = InternalUtils.resolve(this, indexesConstraintsMap, indexes);
    }

    @Override
    public String toString() {
        return String.join("", getLocation());
    }

    /** Category of node. */
    public enum Category {

        TYPE, FIELD, GETTER, PARAMETER,

        ARRAY_COMPONENT,
        ITERABLE_ELEMENT,
        MAP_KEY, MAP_VALUE

    }

}
