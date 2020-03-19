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
import com.github.wautsns.simplevalidator.util.common.CollectionUtils;
import lombok.Getter;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract constrained node.
 *
 * @author wautsns
 * @since Mar 18, 2020
 */
@Getter
public abstract class ConstrainedNode {

    /** location */
    protected final Location location;
    /** type */
    protected final Type type;
    /** constraints */
    protected final List<Annotation> constraints;

    /**
     * Get parent node.
     *
     * @return parent node, or {@code null} if the node is <strong>root</strong>
     */
    public abstract ConstrainedNode getParent();

    /**
     * Get children nodes.
     *
     * @return children nodes
     */
    public abstract List<? extends ConstrainedNode> getChildren();

    /**
     * Require child node.
     *
     * @param name name
     * @return child node
     * @throws IllegalArgumentException if no child is named the specific name
     */
    public ConstrainedNode requireChild(String name) {
        ConstrainedNode child = getChild(name);
        if (child != null) { return child; }
        throw new IllegalArgumentException(String.format("There is no child named '%s' in %s", name, location));
    }

    /**
     * Get child node.
     *
     * @param name name
     * @return child node, or {@code null} if no child is named the specific name
     */
    public ConstrainedNode getChild(String name) {
        for (ConstrainedNode child : getChildren()) {
            if (child.location.getSimpleName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Get criterion wrapper.
     *
     * @return criterion wrapper, or {@code null} if the node is <strong>root</strong>
     */
    public abstract Criterion.Wrapper getCriterionWrapper();

    @Override
    public String toString() {
        return location.toString();
    }

    // -------------------- constructor -------------------------------------------------

    public ConstrainedNode(String name, Type type, Annotation[] annotations) {
        this(new Location(name), type, Arrays.asList(annotations));
    }

    public ConstrainedNode(ConstrainedNode parent, String name, AnnotatedType annotatedType) {
        this(new Location(parent, name), annotatedType);
    }

    public ConstrainedNode(Location location, AnnotatedType annotatedType) {
        this(location, annotatedType.getType(), getAllAnnotations(annotatedType));
    }

    public ConstrainedNode(Location location, Type type, List<Annotation> annotations) {
        this.location = location;
        this.type = type;
        this.constraints = ConstraintUtils.filterOutConstraints(annotations);
    }

    // -------------------- location ----------------------------------------------------

    public static class Location implements Serializable {

        private static final long serialVersionUID = 6613571499661573545L;

        /** node names */
        private final LinkedList<String> names = new LinkedList<>();

        public Location(String name) {
            names.add(name);
        }

        public Location(ConstrainedNode parent, String name) {
            names.addAll(parent.getLocation().names);
            names.add(name);
        }

        public String getSimpleName() {
            return names.getLast();
        }

        @Override
        public String toString() {
            return String.join("", names);
        }

    }

    // -------------------- internal utils ----------------------------------------------

    /**
     * Clear unconstrained nodes.
     *
     * @param nodes nodes
     * @param <N> type of node
     * @return new nodes(unmodified) after clearing
     */
    protected static <N extends ConstrainedNode> List<N> clear(List<N> nodes) {
        nodes = nodes.stream()
                .filter(node -> !node.getChildren().isEmpty() || !node.getConstraints().isEmpty())
                .collect(Collectors.toCollection(LinkedList::new));
        return CollectionUtils.unmodifiableList(nodes);
    }

    private static List<Annotation> getAllAnnotations(AnnotatedType annotatedType) {
        List<Annotation> annotations = new LinkedList<>(Arrays.asList(annotatedType.getDeclaredAnnotations()));
        Type type = annotatedType.getType();
        if (type instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            annotations.addAll(0, Arrays.asList(typeVariable.getDeclaredAnnotations()));
            for (AnnotatedType annotatedBound : typeVariable.getAnnotatedBounds()) {
                annotations.addAll(0, getAllAnnotations(annotatedBound));
            }
        }
        return annotations;
    }

}
