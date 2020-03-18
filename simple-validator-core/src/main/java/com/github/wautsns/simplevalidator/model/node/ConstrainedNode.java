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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
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

    protected final Location location;
    protected final Type type;
    protected final List<Annotation> constraints;

    public abstract ConstrainedNode getParent();

    public abstract List<? extends ConstrainedNode> getChildren();

    public abstract Criterion.Wrapper getCriterionWrapper();

    // -------------------- constructor -------------------------------------------------

    public ConstrainedNode(String name, Type type, Annotation[] annotations) {
        this(new Location(name), type, annotations);
    }

    public ConstrainedNode(ConstrainedNode parent, String name, AnnotatedType annotatedType) {
        this(new Location(parent, name), annotatedType);
    }

    public ConstrainedNode(Location location, AnnotatedType annotatedType) {
        this(location, annotatedType.getType(), annotatedType.getAnnotations());
    }

    public ConstrainedNode(Location location, Type type, Annotation[] annotations) {
        this(location, type, ConstraintUtils.filterOutConstraints(annotations));
    }

    public ConstrainedNode(Location location, Type type, List<Annotation> constraints) {
        this.location = location;
        this.type = type;
        this.constraints = constraints;
    }

    public static class Location {

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

    }

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

}
