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

import com.github.wautsns.simplevalidator.kernal.extractor.type.basic.AnnotatedTypeExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.type.basic.AnnotatedTypeExtractorForAnnotatedArrayType;
import com.github.wautsns.simplevalidator.kernal.extractor.type.builtin.AnnotatedTypeExtractorForIterableElement;
import com.github.wautsns.simplevalidator.kernal.extractor.type.builtin.AnnotatedTypeExtractorForMapKey;
import com.github.wautsns.simplevalidator.kernal.extractor.type.builtin.AnnotatedTypeExtractorForMapValue;
import com.github.wautsns.simplevalidator.kernal.extractor.type.builtin.AnnotatedTypeExtractorForOptionalValue;
import com.github.wautsns.simplevalidator.util.common.CollectionUtils;
import lombok.Getter;

import java.lang.reflect.AnnotatedType;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * The constrained type container.
 *
 * @author wautsns
 * @since Mar 18, 2020
 */
@Getter
public abstract class ConstrainedTypeContainer extends ConstrainedNode {

    /** Annotated type. */
    protected final AnnotatedType annotatedType;
    /** Constrained extracted types. */
    protected final List<ConstrainedExtractedType> extractedTypes;

    @Override
    public List<? extends ConstrainedNode> getChildren() {
        List<? extends ConstrainedNode> superChildren = super.getChildren();
        if (superChildren.isEmpty()) { return extractedTypes; }
        List<ConstrainedNode> children = new LinkedList<>();
        children.addAll(superChildren);
        children.addAll(extractedTypes);
        return CollectionUtils.unmodifiableList(children);
    }

    // #################### constructor ##################################################

    /**
     * Construct a constrained type container.
     *
     * @param name root name
     * @param annotatedType annotated type
     */
    public ConstrainedTypeContainer(String name, AnnotatedType annotatedType) {
        this(new Location(name), annotatedType);
    }

    /**
     * Construct a constrained type container.
     *
     * @param parent parent
     * @param name node name
     * @param annotatedType annotated type
     */
    public ConstrainedTypeContainer(ConstrainedNode parent, String name, AnnotatedType annotatedType) {
        this(new Location(parent, name), annotatedType);
    }

    /**
     * Construct a constrained type container.
     *
     * @param location node location
     * @param annotatedType annotated type
     */
    public ConstrainedTypeContainer(Location location, AnnotatedType annotatedType) {
        super(location, annotatedType);
        this.annotatedType = annotatedType;
        this.extractedTypes = initExtractedTypes(this);
    }

    // #################### utils #######################################################

    /**
     * Initialize extracted types.
     *
     * @param typeContainer type container
     * @return extracted types(unmodified)
     */
    public static List<ConstrainedExtractedType> initExtractedTypes(ConstrainedTypeContainer typeContainer) {
        List<ConstrainedExtractedType> nodes = new LinkedList<>();
        Set<AnnotatedType> extractedTypes = new HashSet<>();
        ANNOTATED_TYPE_EXTRACTORS.values().forEach(extractors -> extractors.forEach(extractor -> {
            AnnotatedType extractedType = extractor.extract(typeContainer.annotatedType);
            if (extractedType == null || extractedTypes.contains(extractedType)) { return; }
            nodes.add(new ConstrainedExtractedType(typeContainer, extractor, extractedType));
            extractedTypes.add(extractedType);
        }));
        return clear(nodes);
    }

    // #################### annotated type extractors ############################################################

    /** Annotated type extractors. */
    private static final TreeMap<Integer, List<AnnotatedTypeExtractor>> ANNOTATED_TYPE_EXTRACTORS = new TreeMap<>();

    static {
        addAnnotatedTypeExtractor(1000, AnnotatedTypeExtractorForIterableElement.INSTANCE);
        addAnnotatedTypeExtractor(2000, AnnotatedTypeExtractorForMapKey.INSTANCE);
        addAnnotatedTypeExtractor(3000, AnnotatedTypeExtractorForMapValue.INSTANCE);
        addAnnotatedTypeExtractor(4000, AnnotatedTypeExtractorForAnnotatedArrayType.INSTANCE);
        addAnnotatedTypeExtractor(5000, AnnotatedTypeExtractorForOptionalValue.INSTANCE);
    }

    /**
     * Add extracted type metadata.
     *
     * <pre>
     * default extracted type metadata are as followers:
     *   1000: {@link AnnotatedTypeExtractorForIterableElement#INSTANCE}
     *   2000: {@link AnnotatedTypeExtractorForMapKey#INSTANCE}
     *   3000: {@link AnnotatedTypeExtractorForMapValue#INSTANCE}
     *   4000: {@link AnnotatedTypeExtractorForAnnotatedArrayType#INSTANCE}
     *   5000: {@link AnnotatedTypeExtractorForOptionalValue#INSTANCE}
     * </pre>
     *
     * @param order order
     * @param annotatedTypeExtractor annotated type extractor
     */
    public static void addAnnotatedTypeExtractor(int order, AnnotatedTypeExtractor annotatedTypeExtractor) {
        ANNOTATED_TYPE_EXTRACTORS.computeIfAbsent(order, i -> new LinkedList<>()).add(annotatedTypeExtractor);
    }

}
