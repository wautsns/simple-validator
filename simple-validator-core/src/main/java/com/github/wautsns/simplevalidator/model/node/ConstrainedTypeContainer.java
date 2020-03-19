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

import com.github.wautsns.simplevalidator.model.node.extractedtype.ConstrainedExtractedType;
import com.github.wautsns.simplevalidator.model.node.extractedtype.metadata.ArrayComponentExtractedTypeMetadata;
import com.github.wautsns.simplevalidator.model.node.extractedtype.metadata.IterableElementExtractedTypeMetadata;
import com.github.wautsns.simplevalidator.model.node.extractedtype.metadata.MapKeyExtractedTypeMetadata;
import com.github.wautsns.simplevalidator.model.node.extractedtype.metadata.MapValueExtractedTypeMetadata;
import lombok.Getter;

import java.lang.reflect.AnnotatedType;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * Constrained type container.
 *
 * @author wautsns
 * @since Mar 18, 2020
 */
@Getter
public abstract class ConstrainedTypeContainer extends ConstrainedNode {

    protected final AnnotatedType annotatedType;
    protected final List<ConstrainedExtractedType> extractedTypes;

    @Override
    public List<? extends ConstrainedNode> getChildren() {
        return getExtractedTypes();
    }

    // -------------------- constructor -------------------------------------------------

    public ConstrainedTypeContainer(String name, AnnotatedType annotatedType) {
        this(new Location(name), annotatedType);
    }

    public ConstrainedTypeContainer(ConstrainedNode parent, String name, AnnotatedType annotatedType) {
        this(new Location(parent, name), annotatedType);
    }

    public ConstrainedTypeContainer(Location location, AnnotatedType annotatedType) {
        super(location, annotatedType);
        this.annotatedType = annotatedType;
        this.extractedTypes = initTypeExtracted(this);
    }

    // -------------------- type contained metadata -------------------------------------

    private static final TreeMap<Integer, List<ConstrainedExtractedType.Metadata>> METADATA = new TreeMap<>();

    static {
        addTypeExtractedMetadata(1000, IterableElementExtractedTypeMetadata.INSTANCE);
        addTypeExtractedMetadata(2000, MapKeyExtractedTypeMetadata.INSTANCE);
        addTypeExtractedMetadata(3000, MapValueExtractedTypeMetadata.INSTANCE);
        addTypeExtractedMetadata(4000, ArrayComponentExtractedTypeMetadata.INSTANCE);
    }

    /**
     * Add extracted type metadata.
     *
     * <ul>
     * default type extracted metadata are as followers:
     * <li>1000: {@link IterableElementExtractedTypeMetadata#INSTANCE}</li>
     * <li>2000: {@link MapKeyExtractedTypeMetadata#INSTANCE}</li>
     * <li>3000: {@link MapValueExtractedTypeMetadata#INSTANCE}</li>
     * <li>4000: {@link ArrayComponentExtractedTypeMetadata#INSTANCE}</li>
     * </ul>
     *
     * @param order order
     * @param metadata extracted type metadata
     * @see ConstrainedTypeContainer#addTypeExtractedMetadata(int, ConstrainedExtractedType.Metadata)
     */
    public static void addTypeExtractedMetadata(int order, ConstrainedExtractedType.Metadata metadata) {
        METADATA.computeIfAbsent(order, i -> new LinkedList<>()).add(metadata);
    }

    public static List<ConstrainedExtractedType> initTypeExtracted(ConstrainedTypeContainer typeContainer) {
        List<ConstrainedExtractedType> tmp = new LinkedList<>();
        Set<AnnotatedType> extractedTypes = new HashSet<>();
        METADATA.values().forEach(metadataList -> metadataList.forEach(metadata -> {
            AnnotatedType extractedType = metadata.extract(typeContainer.annotatedType);
            if (extractedType == null || extractedTypes.contains(extractedType)) { return; }
            tmp.add(new ConstrainedExtractedType(typeContainer, metadata, extractedType));
            extractedTypes.add(extractedType);
        }));
        return clear(tmp);
    }

}
