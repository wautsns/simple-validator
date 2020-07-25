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
package com.github.wautsns.simplevalidator.kernal.node;

import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.exception.analysis.IllegalConstrainedNodeException;
import com.github.wautsns.simplevalidator.kernal.constraint.Constraint;
import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapper;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.ValueExtractor;
import com.github.wautsns.simplevalidator.util.common.CollectionUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Abstract constrained node.
 *
 * @author wautsns
 * @since Mar 18, 2020
 */
@Getter
@RequiredArgsConstructor
public abstract class ConstrainedNode {

    /** Location. */
    protected final Location location;
    /** Type. */
    protected final Type type;
    /** Constraints. */
    protected final List<Constraint<?>> constraints;
    /** Extracted values. */
    protected final List<ConstrainedExtractedValue> extractedValues;

    /**
     * Get parent.
     *
     * @return parent, or {@code null} if the node is <strong>root</strong>
     */
    public abstract ConstrainedNode getParent();

    /**
     * Get children.
     *
     * @return children(unmodified)
     */
    public List<? extends ConstrainedNode> getChildren() {
        return extractedValues;
    }

    /**
     * Require the child with the specified name.
     *
     * @param name child name
     * @return child
     * @throws IllegalArgumentException if there is no child with the specified name
     */
    public ConstrainedNode requireChild(String name) {
        ConstrainedNode child = getChild(name);
        if (child != null) { return child; }
        throw new ConstraintAnalysisException("There is no child named [%s] in [%s]", name, location);
    }

    /**
     * Get the child with the specified name.
     *
     * @param name child name
     * @return child, or {@code null} if there is no child with the specified name
     */
    public ConstrainedNode getChild(String name) {
        return getNode(getChildren(), name);
    }

    /**
     * Get the extracted value with the specified name.
     *
     * @param name child name
     * @return child, or {@code null} if there is no child with the specified name
     */
    public ConstrainedExtractedValue getExtractedValue(String name) {
        return getNode(extractedValues, name);
    }

    /**
     * Get criterion wrapper for the node.
     *
     * @return criterion wrapper, or {@code null} if the node is <strong>root</strong>
     */
    public abstract CriterionWrapper getCriterionWrapper();

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
        this(new Location(name), type, Constraint.filterConstraints(annotations));
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
        this(location, annotatedType.getType(), Constraint.filterConstraints(annotatedType));
    }

    /**
     * Construct a constrained node.
     *
     * @param location node location
     * @param type type
     * @param constraints constraints
     */
    public ConstrainedNode(Location location, Type type, List<Constraint<?>> constraints) {
        this.location = location;
        this.type = type;
        // Get constraints applied to the type(ignore value extractor).
        List<Constraint<?>> constraintsAppliedToTheNode = constraints.stream()
                .filter(constraint -> constraint.appliesTo(type))
                .collect(Collectors.toCollection(LinkedList::new));
        if (constraintsAppliedToTheNode.size() == constraints.size()) {
            // All constraints apply to the type, ok.
            this.constraints = CollectionUtils.unmodifiableList(constraints);
            this.extractedValues = Collections.emptyList();
        } else {
            // If any constraint does not apply to the type, lookup extractors.
            // eg. @VMin applies to int but not applies to OptionalInt directly, and IntExtractorForOptionalInt can make
            // @VMin apply to OptionalInt.
            this.constraints = CollectionUtils.unmodifiableList(constraintsAppliedToTheNode);
            Map<ValueExtractor, List<Constraint<?>>> tmp = new HashMap<>();
            constraints.stream()
                    .filter(constraint -> !constraintsAppliedToTheNode.contains(constraint))
                    .forEach(constraint -> {
                        try {
                            ValueExtractor valueExtractor = constraint.requireApplicableValueExtractor(type);
                            tmp.computeIfAbsent(valueExtractor, i -> new LinkedList<>()).add(constraint);
                        } catch (ConstraintAnalysisException e) {
                            throw new IllegalConstrainedNodeException(e, location, constraint.getOrigin());
                        }
                    });
            this.extractedValues = CollectionUtils.unmodifiableList(tmp.entrySet().stream()
                    .map(entry -> new ConstrainedExtractedValue(this, entry.getKey(), entry.getValue()))
                    .collect(Collectors.toCollection(LinkedList::new)));
        }
    }

    // #################### utils #######################################################

    /** Node location. */
    @EqualsAndHashCode(of = "nodeNames")
    public static class Location implements Serializable {

        private static final long serialVersionUID = 6613571499661573545L;

        /** Node names. */
        private final LinkedList<String> nodeNames = new LinkedList<>();

        /**
         * Construct a root location.
         *
         * @param nodeName node name
         */
        public Location(String nodeName) {
            nodeNames.add(nodeName);
        }

        /**
         * Construct a node location.
         *
         * @param parent parent node
         * @param nodeName node name
         */
        public Location(ConstrainedNode parent, String nodeName) {
            nodeNames.addAll(parent.getLocation().nodeNames);
            nodeNames.add(nodeName);
        }

        /**
         * Construct a node location.
         *
         * @param nodeNames node names
         */
        public Location(List<String> nodeNames) {
            this.nodeNames.addAll(nodeNames);
        }

        /**
         * Copy node names.
         *
         * @return node names
         */
        public LinkedList<String> copyNodeNames() {
            return new LinkedList<>(nodeNames);
        }

        /**
         * Get the simple name of the location(i.e. last node name).
         *
         * @return simple name of the location
         */
        public String getSimpleName() {
            return nodeNames.getLast();
        }

        /**
         * Returns a string representation of the location.
         *
         * @return {@code String.join("", names)}
         */
        @Override
        public String toString() {
            return String.join("", nodeNames);
        }

    }

    // #################### internal utils ##############################################

    /**
     * Get node with the specified name.
     *
     * @param nodes nodes
     * @param name name
     * @param <N> type of node
     * @return node with the specified name, or {@code null} if there is no node with the specified name
     */
    protected static <N extends ConstrainedNode> N getNode(List<N> nodes, String name) {
        return nodes.stream()
                .filter(node -> node.location.getSimpleName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Clear unconstrained node list.
     *
     * @param nodeList node list
     * @param <N> type of node
     * @return nodes(unmodified) after clearing
     */
    protected static <N extends ConstrainedNode> List<N> clear(List<N> nodeList) {
        List<N> tmp = nodeList.stream()
                .filter(node -> !node.getChildren().isEmpty() || !node.getConstraints().isEmpty())
                .collect(Collectors.toCollection(LinkedList::new));
        if (tmp.size() == nodeList.size()) { tmp = nodeList; }
        return CollectionUtils.unmodifiableList(tmp);
    }

}
