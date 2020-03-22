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

import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.exception.analysis.IllegalConstrainedNodeException;
import com.github.wautsns.simplevalidator.model.constraint.Constraint;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.node.extraction.value.ConstrainedExtractedValue;
import com.github.wautsns.simplevalidator.util.common.CollectionUtils;
import com.github.wautsns.simplevalidator.util.extractor.ValueExtractor;
import lombok.Getter;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    /** constraint list */
    protected final List<Constraint<?>> constraintList;
    /** extracted value list */
    protected final List<ConstrainedExtractedValue> extractedValueList;

    /**
     * Get parent.
     *
     * @return parent, or {@code null} if the node is <strong>root</strong>
     */
    public abstract ConstrainedNode getParent();

    /**
     * Get child list.
     *
     * @return child list(unmodified)
     */
    public List<? extends ConstrainedNode> getChildList() {
        return extractedValueList;
    }

    /**
     * Require the child named the specified name.
     *
     * @param name child name
     * @return child
     * @throws IllegalArgumentException if there is no child named the specified name
     */
    public ConstrainedNode requireChild(String name) {
        ConstrainedNode child = getChild(name);
        if (child != null) { return child; }
        throw new IllegalArgumentException(String.format("There is no child named '%s' in %s", name, location));
    }

    /**
     * Get the child named the specified name.
     *
     * @param name child name
     * @return child, or {@code null} if there is no child named the specified name
     */
    public ConstrainedNode getChild(String name) {
        return getChildList().stream()
                .filter(child -> child.location.getSimpleName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the extracted value named the specified name.
     *
     * @param name child name
     * @return child, or {@code null} if there is no child named the specified name
     */
    public ConstrainedExtractedValue getExtractedValue(String name) {
        return extractedValueList.stream()
                .filter(extractedValue -> extractedValue.location.getSimpleName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get criterion wrapper for the node.
     *
     * @return criterion wrapper, or {@code null} if the node is <strong>root</strong>
     */
    public abstract Criterion.Wrapper getCriterionWrapper();

    @Override
    public final int hashCode() {
        return location.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (obj == null) { return false; }
        if (this.getClass() != obj.getClass()) { return false; }
        ConstrainedNode that = (ConstrainedNode) obj;
        return this.location.equals(that.location);
    }

    @Override
    public final String toString() {
        return location.toString();
    }

    // #################### constructor #################################################

    /**
     * Construct a constrained <strong>root</strong>.
     *
     * @param name root name
     * @param type type
     * @param annotations annotations
     */
    public ConstrainedNode(String name, Type type, Annotation[] annotations) {
        this(new Location(name), type, Constraint.filterOutConstraintList(annotations));
    }

    /**
     * Construct a constrained node.
     *
     * @param parent parent node
     * @param name node name
     * @param annotatedType annotated type
     */
    public ConstrainedNode(ConstrainedNode parent, String name, AnnotatedType annotatedType) {
        this(new Location(parent, name), annotatedType);
    }

    /**
     * Construct a constrained node.
     *
     * @param location node location
     * @param annotatedType annotated type
     */
    public ConstrainedNode(Location location, AnnotatedType annotatedType) {
        this(location, annotatedType.getType(), Constraint.filterOutConstraintList(annotatedType));
    }

    /**
     * Construct a constrained node.
     *
     * @param location node location
     * @param type type
     * @param constraintList constraint list
     */
    public ConstrainedNode(Location location, Type type, List<Constraint<?>> constraintList) {
        this.location = location;
        this.type = type;
        List<Constraint<?>> constraintsAppliedToTheNode = constraintList.stream()
                .filter(constraint -> constraint.appliesTo(type))
                .collect(Collectors.toCollection(LinkedList::new));
        if (constraintsAppliedToTheNode.size() == constraintList.size()) {
            this.constraintList = CollectionUtils.unmodifiableList(constraintList);
            this.extractedValueList = Collections.emptyList();
        } else {
            this.constraintList = CollectionUtils.unmodifiableList(constraintsAppliedToTheNode);
            Map<ValueExtractor, List<Constraint<?>>> tmp = new HashMap<>();
            constraintList.stream()
                    .filter(constraint -> !constraintsAppliedToTheNode.contains(constraint))
                    .forEach(constraint -> {
                        try {
                            ValueExtractor valueExtractor = constraint.requireApplicableValueExtractor(type);
                            tmp.computeIfAbsent(valueExtractor, i -> new LinkedList<>()).add(constraint);
                        } catch (ConstraintAnalysisException e) {
                            throw new IllegalConstrainedNodeException(e, location, constraint.getOrigin());
                        }
                    });
            this.extractedValueList = CollectionUtils.unmodifiableList(tmp.entrySet().stream()
                    .map(entry -> new ConstrainedExtractedValue(this, entry.getKey(), entry.getValue()))
                    .collect(Collectors.toCollection(LinkedList::new)));
        }
    }

    public ConstrainedNode(
            Location location, Type type, List<Constraint<?>> constraintList,
            List<ConstrainedExtractedValue> extractedValueList) {
        this.location = Objects.requireNonNull(location);
        this.type = Objects.requireNonNull(type);
        this.constraintList = Objects.requireNonNull(constraintList);
        this.extractedValueList = Objects.requireNonNull(extractedValueList);
    }

    // #################### utils #######################################################

    // ==================== location ====================================================

    /** node location */
    public static class Location implements Serializable {

        private static final long serialVersionUID = 6613571499661573545L;

        /** node names */
        private final LinkedList<String> names = new LinkedList<>();

        /**
         * Construct a root location.
         *
         * @param name root name
         */
        public Location(String name) {
            names.add(name);
        }

        /**
         * Construct a node location.
         *
         * @param parent parent node
         * @param name node name
         */
        public Location(ConstrainedNode parent, String name) {
            names.addAll(parent.getLocation().names);
            names.add(name);
        }

        /**
         * Get the simple name of the location(i.e. last node name).
         *
         * @return simple name of the location
         */
        public String getSimpleName() {
            return names.getLast();
        }

        @Override
        public int hashCode() {
            return names.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) { return true; }
            if (obj == null) { return false; }
            if (this.getClass() != obj.getClass()) { return false; }
            Location that = (Location) obj;
            return this.names.equals(that.names);
        }

        /**
         * Returns a string representation of the location.
         *
         * @return {@code String.join("", names)}
         */
        @Override
        public String toString() {
            return String.join("", names);
        }

    }

    // #################### internal utils ##############################################

    /**
     * Clear unconstrained node list.
     *
     * @param nodeList node list
     * @param <N> type of node
     * @return nodes(unmodified) after clearing
     */
    protected static <N extends ConstrainedNode> List<N> clear(List<N> nodeList) {
        List<N> tmp = nodeList.stream()
                .filter(node -> !node.getChildList().isEmpty() || !node.getConstraintList().isEmpty())
                .collect(Collectors.toCollection(LinkedList::new));
        if (tmp.size() == nodeList.size()) { tmp = nodeList; }
        return CollectionUtils.unmodifiableList(tmp);
    }

}
