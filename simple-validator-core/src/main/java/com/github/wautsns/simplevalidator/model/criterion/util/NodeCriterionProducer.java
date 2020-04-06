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
package com.github.wautsns.simplevalidator.model.criterion.util;

import com.github.wautsns.simplevalidator.model.constraint.Constraint;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Node criterion producer.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
class NodeCriterionProducer {

    /** root */
    private final ConstrainedNode root;
    /** disordered constrained node -> criterion processor map */
    private final Map<ConstrainedNode, List<Constraint<?>.CriterionProcessor>>
            disorderedNodeCriterionProcessorMap = new LinkedHashMap<>();
    /** ordered constrained node -> criterion processor map */
    private final Map<Integer, LinkedHashMap<ConstrainedNode, List<Constraint<?>.CriterionProcessor>>>
            orderedNodeCriterionProcessorMap = new TreeMap<>(Constraint.ORDER_COMPARATOR);

    /**
     * Produce criterion.
     *
     * @return criterion, or {@code null} if the criterion is unnecessary
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Criterion produce() {
        Criteria criteria = Criteria.newInstance(root.getType());
        orderedNodeCriterionProcessorMap.forEach((order, nodeCriterionProcessorMap) -> {
            Map<ConstrainedNode, Criteria> nodeCriteriaMap = new LinkedHashMap<>();
            process(nodeCriteriaMap, disorderedNodeCriterionProcessorMap);
            process(nodeCriteriaMap, nodeCriterionProcessorMap);
            if (nodeCriteriaMap.isEmpty()) { return; }
            while (!(nodeCriteriaMap.size() == 1 && nodeCriteriaMap.containsKey(root))) {
                new LinkedHashMap<>(nodeCriteriaMap).forEach((node, wip) -> {
                    if (node == root) { return; }
                    Criterion criterion = wip.simplify();
                    if (criterion == null) { return; }
                    Criteria parentWip = nodeCriteriaMap.computeIfAbsent(
                            node.getParent(), n -> Criteria.newInstance(n.getType()));
                    parentWip.add(node.getCriterionWrapper().wrap(criterion));
                    nodeCriteriaMap.remove(node);
                });
            }
            criteria.add(nodeCriteriaMap.values().iterator().next().simplify());
        });
        return criteria.simplify();
    }

    /**
     * Process node criteria map.
     *
     * @param nodeCriteriaMap node criteria map
     * @param nodeCriterionProcessorMap node criterion processor map
     */
    @SuppressWarnings("rawtypes")
    private void process(
            Map<ConstrainedNode, Criteria> nodeCriteriaMap,
            Map<ConstrainedNode, List<Constraint<?>.CriterionProcessor>> nodeCriterionProcessorMap) {
        nodeCriterionProcessorMap.forEach((node, processorList) -> {
            if (node.getConstraints().isEmpty()) { return; }
            Criteria wip = nodeCriteriaMap.computeIfAbsent(node, n -> Criteria.newInstance(n.getType()));
            processorList.forEach(processor -> processor.process(node, wip));
        });
    }

    // #################### constructor #################################################

    /**
     * Construct a node criterion producer.
     *
     * @param root root
     */
    public NodeCriterionProducer(ConstrainedNode root) {
        this.root = root;
        add(root);
        simplify();
    }

    /**
     * Add a constrained node.
     *
     * @param node constrained node
     */
    private void add(ConstrainedNode node) {
        node.getConstraints().forEach(constraint -> add(node, constraint));
        node.getChildren().forEach(this::add);
    }

    /**
     * Add a constrained node with the constraint.
     *
     * @param node constrained node
     * @param constraint constraint
     */
    private void add(ConstrainedNode node, Constraint<?> constraint) {
        Integer order = constraint.getOrder();
        Map<ConstrainedNode, List<Constraint<?>.CriterionProcessor>> target;
        if (order == null) {
            target = disorderedNodeCriterionProcessorMap;
        } else {
            target = orderedNodeCriterionProcessorMap.computeIfAbsent(order, i -> new LinkedHashMap<>());
        }
        target.computeIfAbsent(node, ignored -> new LinkedList<>()).add(constraint.getCriterionProcessor());
    }

    /**
     * Simplify the ordered node criterion processor map. e.g.
     *
     * <pre>
     * public class Human {
     *
     *     // if not simplified, reflection will be called twice, but obviously superfluous in this case
     *     &#64;VNotBlank(order = 1)
     *     &#64;VPattern(regex = ".+", order = 2)
     *     private String name;
     *     &#64;VDomain(value = "[18,25)", order = 3)
     *     private int age;
     * }
     * </pre>
     */
    private void simplify() {
        Iterator<Entry<Integer, LinkedHashMap<ConstrainedNode, List<Constraint<?>.CriterionProcessor>>>> iterator
                = orderedNodeCriterionProcessorMap.entrySet().iterator();
        Map<ConstrainedNode, List<Constraint<?>.CriterionProcessor>> prev = null;
        while (iterator.hasNext()) {
            Entry<Integer, LinkedHashMap<ConstrainedNode, List<Constraint<?>.CriterionProcessor>>> curr
                    = iterator.next();
            if (prev == null) {
                prev = curr.getValue();
            } else if (prev.size() == 1) {
                Entry<ConstrainedNode, List<Constraint<?>.CriterionProcessor>> prevNode
                        = prev.entrySet().iterator().next();
                List<Constraint<?>.CriterionProcessor> processors = curr.getValue().remove(prevNode.getKey());
                if (processors == null) { continue; }
                prevNode.getValue().addAll(processors);
                if (curr.getValue().isEmpty()) { iterator.remove(); }
            }
        }
    }

}
