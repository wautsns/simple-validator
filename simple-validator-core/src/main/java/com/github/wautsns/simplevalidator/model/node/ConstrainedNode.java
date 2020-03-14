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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Abstract constrained node.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Getter
@RequiredArgsConstructor
public abstract class ConstrainedNode {

    /** type */
    protected final Type type;
    /** location */
    protected final Location location;

    /**
     * Get parent node.
     *
     * @return parent node, or {@code null} if the node is root
     */
    public abstract ConstrainedNode getParent();

    /**
     * Get children nodes.
     *
     * @return children nodes
     */
    public abstract List<? extends ConstrainedNode> getChildren();

    /**
     * Get name of the node.
     *
     * @return name of the node
     */
    public String getName() {
        return location.getSimpleName();
    }

    /**
     * Require child node.
     *
     * @param name name
     * @return child node, or {@code null} if no child is named the specific name
     */
    public ConstrainedNode requireChild(String name) {
        return Objects.requireNonNull(
                getChild(name),
                String.format("There is no child named %s, or the child is not constrained.", name));
    }

    /**
     * Get child node.
     *
     * @param name name
     * @return child node, or {@code null} if no child is named the specific name
     */
    public ConstrainedNode getChild(String name) {
        for (ConstrainedNode child : getChildren()) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Get constraints(nonnull unmodified) on the node.
     *
     * @return constraints(nonnull unmodified) on the node
     */
    public abstract List<Annotation> getConstraints();

    /**
     * Get criterion wrapper.
     *
     * @return criterion wrapper, or {@code null} if the node is root.
     */
    public abstract Criterion.Wrapper getCriterionWrapper();

    @Override
    public final int hashCode() {
        return location.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (this.getClass() != obj.getClass()) { return false; }
        ConstrainedNode that = (ConstrainedNode) obj;
        return this.location.equals(that.location);
    }

    @Override
    public final String toString() {
        return location.toString();
    }

    @EqualsAndHashCode
    public static class Location implements Serializable {

        private static final long serialVersionUID = 6613571499661573545L;

        /** names of nodes */
        private final LinkedList<String> names = new LinkedList<>();

        public Location(String name) {
            names.add(name);
        }

        public Location(ConstrainedNode parent, String name) {
            names.addAll(parent.getLocation().names);
            names.add(name);
        }

        /**
         * Get the simple name.
         *
         * @return the simple name
         */
        public String getSimpleName() {
            return names.getLast();
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            names.forEach(result::append);
            return result.toString();
        }

    }

    /**
     * Clear unconstrained node.
     *
     * @param nodes nodes
     * @param <N> type of node
     * @return new unmodified nodes after clearing
     */
    protected static <N extends ConstrainedNode> List<N> clear(List<N> nodes) {
        nodes = nodes.stream()
                .filter(node -> !node.getChildren().isEmpty() || !node.getConstraints().isEmpty())
                .collect(Collectors.toCollection(LinkedList::new));
        return CollectionUtils.unmodifiableList(nodes);
    }

}
