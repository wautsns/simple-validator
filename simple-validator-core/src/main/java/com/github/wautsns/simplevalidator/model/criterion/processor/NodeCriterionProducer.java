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
package com.github.wautsns.simplevalidator.model.criterion.processor;

import com.github.wautsns.simplevalidator.model.criterion.kernel.Criteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.Criterion;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.ConstraintUtils;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * Node criterion producer.
 *
 * TODO doc
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class NodeCriterionProducer {

    private final ConstrainedNode root;
    private final LinkedHashMap<ConstrainedNode, List<ConstraintCriterionProcessor<?>>>
            noOrderNodeConstraintCriterionProcessorsMap
            = new LinkedHashMap<>();
    private final Map<Integer, LinkedHashMap<ConstrainedNode, List<ConstraintCriterionProcessor<?>>>>
            orderedNodeConstraintCriterionProcessors = new TreeMap<>(
            ConstraintUtils::compareOrder);

    public NodeCriterionProducer(ConstrainedNode root) {
        this.root = root;
        add(root);
        simplify();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <C extends Criterion> C produce() {
        Criteria criteria = ProcessUtils.newCriteriaFor(root.getType());
        orderedNodeConstraintCriterionProcessors
                .forEach((order, orderedConstrainedNodeProcessorsMap) -> {
                    Map<ConstrainedNode, Criteria> tmp = new LinkedHashMap<>();
                    process(tmp, noOrderNodeConstraintCriterionProcessorsMap);
                    process(tmp, orderedConstrainedNodeProcessorsMap);
                    if (tmp.isEmpty()) { return; }
                    while (!(tmp.size() == 1 && tmp.containsKey(root))) {
                        new LinkedHashMap<>(tmp).forEach((el, wip) -> {
                            if (el == root) { return; }
                            Criterion criterion = wip.simplify();
                            if (criterion == null) { return; }
                            Criteria superiorWip = tmp.computeIfAbsent(
                                    el.getParent(), node -> ProcessUtils.newCriteriaFor(node.getType()));
                            superiorWip.add(ProcessUtils.wrapCriterionToSuitParentNode(el, criterion));
                            tmp.remove(el);
                        });
                    }
                    criteria.add(tmp.values().iterator().next().simplify());
                });
        return (C) criteria.simplify();
    }

    @SuppressWarnings("rawtypes")
    private void process(
            Map<ConstrainedNode, Criteria> nodeCriteriaMap,
            Map<ConstrainedNode, List<ConstraintCriterionProcessor<?>>> nodeProcessorsMap) {
        nodeProcessorsMap.forEach((node, processors) -> {
            if (node.getConstraints().isEmpty()) { return; }
            Criteria wip = nodeCriteriaMap.computeIfAbsent(
                    node, n -> ProcessUtils.newCriteriaFor(n.getType()));
            processors.forEach(processor -> processor.process(node, wip));
        });
    }

    // ------------------------- initialize utils -----------------------------------

    private void add(ConstrainedNode node) {
        node.getConstraints().forEach(constraint -> add(node, constraint));
        node.getChildren().forEach(this::add);
    }

    private void add(ConstrainedNode node, Annotation constraint) {
        Integer order = ConstraintUtils.getOrder(constraint);
        Map<ConstrainedNode, List<ConstraintCriterionProcessor<?>>> target;
        if (order == null) {
            target = noOrderNodeConstraintCriterionProcessorsMap;
        } else {
            target = orderedNodeConstraintCriterionProcessors.computeIfAbsent(order, i -> new LinkedHashMap<>());
        }
        target.computeIfAbsent(node, ignored -> new LinkedList<>()).add(new ConstraintCriterionProcessor<>(constraint));
    }

    private void simplify() {
        Set<Entry<Integer, LinkedHashMap<ConstrainedNode, List<ConstraintCriterionProcessor<?>>>>> entrySet
                = orderedNodeConstraintCriterionProcessors.entrySet();
        Map<ConstrainedNode, List<ConstraintCriterionProcessor<?>>> prev = null;
        Iterator<Entry<Integer, LinkedHashMap<ConstrainedNode, List<ConstraintCriterionProcessor<?>>>>> iterator
                = entrySet.iterator();
        while (iterator.hasNext()) {
            Entry<Integer, LinkedHashMap<ConstrainedNode, List<ConstraintCriterionProcessor<?>>>> entry
                    = iterator.next();
            if (prev == null) {
                prev = entry.getValue();
            } else {
                Entry<ConstrainedNode, List<ConstraintCriterionProcessor<?>>> tmp = prev.entrySet().iterator().next();
                List<ConstraintCriterionProcessor<?>> processors = entry.getValue().get(tmp.getKey());
                if (processors != null) {
                    entry.getValue().remove(tmp.getKey());
                    tmp.getValue().addAll(processors);
                    if (entry.getValue().isEmpty()) {
                        iterator.remove();
                    }
                }
            }
        }
    }

}
