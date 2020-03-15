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

import com.github.wautsns.simplevalidator.model.node.specific.ConstrainedArrayComponent;
import com.github.wautsns.simplevalidator.model.node.specific.ConstrainedIterableElement;
import com.github.wautsns.simplevalidator.model.node.specific.ConstrainedMapKey;
import com.github.wautsns.simplevalidator.model.node.specific.ConstrainedMapValue;
import com.github.wautsns.simplevalidator.util.ConstraintUtils;
import com.github.wautsns.simplevalidator.util.common.CollectionUtils;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract constrained type node.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Getter
public abstract class ConstrainedType extends ConstrainedNode {

    /** constraints */
    protected final List<Annotation> constraints;
    /** type arg nodes */
    protected final List<ConstrainedTypeArg> typeArgs;

    /**
     * Get type arg node.
     *
     * @param name name
     * @return type arg node, or {@code null} if no type arg named the specific name
     */
    public ConstrainedTypeArg getTypeArg(String name) {
        return typeArgs.stream()
                .filter(typeArg -> typeArg.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get type arg nodes.
     *
     * <p>The method is equal to {@link #getTypeArgs()}
     *
     * @return type arg nodes
     * @see #getTypeArgs()
     */
    @Override
    public List<ConstrainedTypeArg> getChildren() {
        return getTypeArgs();
    }

    protected ConstrainedType(Type type, Location location, AnnotatedType annotatedType) {
        this(type, location, Collections.emptyList(), ConstraintUtils.getIndexesConstraints(annotatedType));
    }

    protected ConstrainedType(
            Type type, Location location, List<Short> indexes, Map<List<Short>, List<Annotation>> indexesConstraints) {
        super(type, location);
        this.constraints = CollectionUtils.removeAndGet(indexesConstraints, indexes, Collections.emptyList());
        this.typeArgs = initTypeArgs(this, indexes, indexesConstraints);
    }

    // -------------------- utils -------------------------------------------------------

    public static List<ConstrainedTypeArg> initTypeArgs(
            ConstrainedType parent, List<Short> indexesOfParent,
            Map<List<Short>, List<Annotation>> indexesConstraints) {
        if (indexesConstraints.isEmpty()) { return Collections.emptyList(); }
        Type type = parent.getType();
        Type componentType = TypeUtils.getComponentType(type);
        if (componentType != null) {
            List<Short> indexes = new LinkedList<>(indexesOfParent);
            indexes.add((short) 0);
            return clear(Collections.singletonList(
                    new ConstrainedArrayComponent(parent, componentType, indexes, indexesConstraints)));
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type rawType = pType.getRawType();
            Type[] typeArgs = pType.getActualTypeArguments();
            Map<Short, ConstrainedTypeArg.Factory> factories = TypeArgsFactories.get(rawType);
            List<ConstrainedTypeArg> nodes = new LinkedList<>();
            for (short i = 0; i < typeArgs.length; i++) {
                ConstrainedTypeArg.Factory factory = factories.get(i);
                if (factory == null) { continue; }
                List<Short> indexes = new LinkedList<>(indexesOfParent);
                indexes.add((short) 0);
                nodes.add(factory.produce(parent, typeArgs[i], indexes, indexesConstraints));
            }
            return clear(nodes);
        } else {
            return Collections.emptyList();
        }
    }

    // -------------------- type arg factories ------------------------------------------

    static {
        TypeArgsFactories.add(new ConstrainedIterableElement.Factory());
        TypeArgsFactories.add(new ConstrainedMapKey.Factory());
        TypeArgsFactories.add(new ConstrainedMapValue.Factory());
    }

    /** type args factories */
    @UtilityClass
    public static class TypeArgsFactories {

        /** factories(order sensitive) TODO see #add(ConstrainedTypeArg.Factory) and add doc */
        private static final LinkedList<Map<Short, ConstrainedTypeArg.Factory>> DATA = new LinkedList<>();

        /**
         * Get type args factories of the specific type.
         *
         * @param type type
         * @return type args factories(thread-safe)
         */
        public static synchronized Map<Short, ConstrainedTypeArg.Factory> get(Type type) {
            for (Map<Short, ConstrainedTypeArg.Factory> factories : DATA) {
                ConstrainedTypeArg.Factory factory = factories.entrySet().iterator().next().getValue();
                if (TypeUtils.isAssignableTo(type, factory.getTypeClass())
                        && TypeUtils.isAssignableToAll(type, factory.getTypeExtraInterfaces())) {
                    return factories;
                }
            }
            return Collections.emptyMap();
        }

        /**
         * Add type arg factory.
         *
         * @param factory type arg factory
         */
        public static synchronized void add(ConstrainedTypeArg.Factory factory) {
            Class<?> clazz = factory.getTypeClass();
            for (int i = 0; i < DATA.size(); i++) {
                Map<Short, ConstrainedTypeArg.Factory> factories = DATA.get(i);
                Class<?> ref = factories.entrySet().iterator().next().getValue().getTypeClass();
                if (clazz == ref) {
                    factories.put(factory.getTypeArgIndex(), factory);
                    return;
                } else if (ref.isAssignableFrom(clazz)) {
                    Map<Short, ConstrainedTypeArg.Factory> tmp = new ConcurrentHashMap<>(4);
                    tmp.put(factory.getTypeArgIndex(), factory);
                    DATA.add(i, tmp);
                    return;
                }
            }
            Map<Short, ConstrainedTypeArg.Factory> tmp = new ConcurrentHashMap<>(4);
            tmp.put(factory.getTypeArgIndex(), factory);
            DATA.addLast(tmp);
        }

    }

}
