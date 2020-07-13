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

import com.github.wautsns.simplevalidator.model.node.extraction.type.ConstrainedExtractedType;
import com.github.wautsns.simplevalidator.model.node.extraction.type.metadata.ExtractedArrayComponentTypeMetadata;
import com.github.wautsns.simplevalidator.model.node.extraction.type.metadata.ExtractedIterableElementTypeMetadata;
import com.github.wautsns.simplevalidator.model.node.extraction.type.metadata.ExtractedMapKeyTypeMetadata;
import com.github.wautsns.simplevalidator.model.node.extraction.type.metadata.ExtractedMapValueTypeMetadata;
import com.github.wautsns.simplevalidator.model.node.extraction.type.metadata.ExtractedOptionalValueTypeMetadata;
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
        METADATA.values().forEach(metadataList -> metadataList.forEach(metadata -> {
            AnnotatedType extractedType = metadata.extract(typeContainer.annotatedType);
            if (extractedType == null || extractedTypes.contains(extractedType)) { return; }
            nodes.add(new ConstrainedExtractedType(typeContainer, metadata, extractedType));
            extractedTypes.add(extractedType);
        }));
        return clear(nodes);
    }

    // #################### extracted type metadata #####################################

    /** Extracted type metadata cache. */
    private static final TreeMap<Integer, List<ConstrainedExtractedType.Metadata>> METADATA = new TreeMap<>();

    static {
        addExtractedTypeMetadata(1000, ExtractedIterableElementTypeMetadata.INSTANCE);
        addExtractedTypeMetadata(2000, ExtractedMapKeyTypeMetadata.INSTANCE);
        addExtractedTypeMetadata(3000, ExtractedMapValueTypeMetadata.INSTANCE);
        addExtractedTypeMetadata(4000, ExtractedArrayComponentTypeMetadata.INSTANCE);
        addExtractedTypeMetadata(5000, ExtractedOptionalValueTypeMetadata.INSTANCE);
    }

    /**
     * Add extracted type metadata.
     *
     * <ul>
     * default extracted type metadata are as followers:
     * <li>1000: {@link ExtractedIterableElementTypeMetadata#INSTANCE}</li>
     * <li>2000: {@link ExtractedMapKeyTypeMetadata#INSTANCE}</li>
     * <li>3000: {@link ExtractedMapValueTypeMetadata#INSTANCE}</li>
     * <li>4000: {@link ExtractedArrayComponentTypeMetadata#INSTANCE}</li>
     * <li>5000: {@link ExtractedOptionalValueTypeMetadata#INSTANCE}</li>
     * </ul>
     *
     * @param order order
     * @param metadata extracted type metadata
     * @see ConstrainedTypeContainer#addExtractedTypeMetadata(int, ConstrainedExtractedType.Metadata)
     */
    public static void addExtractedTypeMetadata(int order, ConstrainedExtractedType.Metadata metadata) {
        METADATA.computeIfAbsent(order, i -> new LinkedList<>()).add(metadata);
    }

}
