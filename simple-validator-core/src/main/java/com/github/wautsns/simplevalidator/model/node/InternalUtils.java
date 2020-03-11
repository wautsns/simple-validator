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

import com.github.wautsns.simplevalidator.model.node.ConstrainedNode.Category;
import com.github.wautsns.simplevalidator.util.ConstraintUtils;
import com.github.wautsns.simplevalidator.util.normal.CollectionUtils;
import com.github.wautsns.simplevalidator.util.normal.ReflectionUtils;
import com.github.wautsns.simplevalidator.util.normal.TypeUtils;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@UtilityClass
class InternalUtils {

    /**
     * Get element name.
     *
     * @param category category
     * @param origin original element
     * @return element name
     */
    public static String getElementName(Category category, AnnotatedElement origin) {
        switch (category) {
            case TYPE:
                return ((Class<?>) origin).getName();
            case FIELD:
                return '#' + ((Field) origin).getName();
            case GETTER:
                return '#' + ReflectionUtils.getPropertyName((Method) origin);
            case ARRAY_COMPONENT:
                return "@component";
            case ITERABLE_ELEMENT:
                return "@element";
            case MAP_KEY:
                return "@key";
            case MAP_VALUE:
                return "@value";
            case PARAMETER:
                Parameter parameter = (Parameter) origin;
                Executable executable = parameter.getDeclaringExecutable();
                Class<?> clazz = executable.getDeclaringClass();
                StringBuilder name = new StringBuilder();
                name.append(clazz.getName());
                name.append('#').append(executable.getName());
                String args = Arrays.stream(executable.getParameterTypes())
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(",", "(", ")"));
                name.append(args);
                name.append('@').append(parameter.getName());
                return name.toString();
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Resolve element.
     *
     * @param indexesConstraintsMap indexes constraints map
     * @param parent parent
     * @param indexes indexes of parent
     * @return children
     */
    public static List<ConstrainedNode> resolve(
            ConstrainedNode parent,
            Map<List<Short>, List<Annotation>> indexesConstraintsMap, List<Short> indexes) {
        switch (parent.getCategory()) {
            case TYPE:
                return resolveClass(parent);
            case FIELD:
            case GETTER:
            case ITERABLE_ELEMENT:
            case MAP_KEY:
            case MAP_VALUE:
            case ARRAY_COMPONENT:
            case PARAMETER:
                return resolveType(indexesConstraintsMap, parent, indexes);
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Resolve class.
     *
     * @param parent parent
     * @return children
     */
    private static List<ConstrainedNode> resolveClass(ConstrainedNode parent) {
        Class<?> origin = (Class<?>) parent.getOrigin();
        List<ConstrainedNode> children = new LinkedList<>();
        ReflectionUtils.forEachPropertyField(origin, field -> {
            field.setAccessible(true);
            ConstrainedNode child = new ConstrainedNode(
                    Category.FIELD,
                    parent, field, field.getGenericType(),
                    IndexesConstraintsUtils.resolve(field.getAnnotatedType()), Collections.emptyList());
            children.add(child);
        });
        ReflectionUtils.forEachPropertyGetter(origin, (name, method) -> {
            method.setAccessible(true);
            ConstrainedNode child = new ConstrainedNode(
                    Category.GETTER,
                    parent, method, method.getGenericReturnType(),
                    IndexesConstraintsUtils.resolve(method.getAnnotatedReturnType()), Collections.emptyList());
            children.add(child);
        });
        return clearNodes(children);
    }

    /**
     * Resolve type.
     *
     * @param indexesConstraintsMap indexes constraints map
     * @param parent parent
     * @param indexes indexes of parent
     * @return children
     */
    private static List<ConstrainedNode> resolveType(
            Map<List<Short>, List<Annotation>> indexesConstraintsMap,
            ConstrainedNode parent, List<Short> indexes) {
        Type type = parent.getType();
        if (type instanceof Class) {
            Class<?> componentType = ((Class<?>) type).getComponentType();
            if (componentType == null) { return Collections.emptyList(); }
            return resolveArray(indexesConstraintsMap, parent, componentType, indexes);
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return resolveArray(indexesConstraintsMap, parent, componentType, indexes);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return resolveParameterizedType(indexesConstraintsMap, parent, parameterizedType, indexes);
        }
        return Collections.emptyList();
    }

    /**
     * Resolve array type.
     *
     * @param indexesConstraintsMap indexes constraints map
     * @param parent parent
     * @param componentType array component type
     * @param indexes indexes of parent
     * @return children
     */
    private static List<ConstrainedNode> resolveArray(
            Map<List<Short>, List<Annotation>> indexesConstraintsMap,
            ConstrainedNode parent, Type componentType, List<Short> indexes) {
        List<ConstrainedNode> children = new LinkedList<>();
        List<Short> componentIndexes = new LinkedList<>(indexes);
        componentIndexes.add((short) 0);
        ConstrainedNode component = new ConstrainedNode(
                Category.ARRAY_COMPONENT,
                parent, null, componentType,
                indexesConstraintsMap, componentIndexes);
        children.add(component);
        return clearNodes(children);
    }

    /**
     * Resolve parameterized type.
     *
     * @param indexesConstraintsMap indexes constraints map
     * @param parent parent
     * @param type type of parent
     * @param indexes indexes of superior
     * @return children
     */
    private static List<ConstrainedNode> resolveParameterizedType(
            Map<List<Short>, List<Annotation>> indexesConstraintsMap,
            ConstrainedNode parent, ParameterizedType type, List<Short> indexes) {
        Type rawType = type.getRawType();
        Type[] typeArguments = type.getActualTypeArguments();
        if (typeArguments.length == 1) {
            if (TypeUtils.isAssignableTo(rawType, Iterable.class)) {
                return initChildrenByTypeArguments(
                        indexesConstraintsMap,
                        parent, indexes, typeArguments,
                        Category.ITERABLE_ELEMENT);
            }
        } else if (typeArguments.length == 2) {
            if (TypeUtils.isAssignableTo(rawType, Map.class)) {
                return initChildrenByTypeArguments(
                        indexesConstraintsMap,
                        parent, indexes, typeArguments,
                        Category.MAP_KEY,
                        Category.MAP_VALUE);
            }
        }
        return Collections.emptyList();
    }

    /**
     * Initialize children by type arguments.
     *
     * @param indexesConstraintsMap indexes constraints map
     * @param parent parent
     * @param typeArguments type arguments of parent node type
     * @param indexes indexes of parent
     * @param categories categories of type arguments
     * @return children
     */
    private static List<ConstrainedNode> initChildrenByTypeArguments(
            Map<List<Short>, List<Annotation>> indexesConstraintsMap,
            ConstrainedNode parent, List<Short> indexes, Type[] typeArguments,
            Category... categories) {
        List<ConstrainedNode> children = new LinkedList<>();
        for (int i = 0; i < categories.length; i++) {
            List<Short> typeArgumentIndexes = new LinkedList<>(indexes);
            typeArgumentIndexes.add((short) i);
            ConstrainedNode node = new ConstrainedNode(
                    categories[i],
                    parent, null, typeArguments[i],
                    indexesConstraintsMap, typeArgumentIndexes);
            children.add(node);
        }
        return clearNodes(children);
    }

    /**
     * Clear unnecessary nodes.
     *
     * @param nodes nodes
     * @return nodes after clearing
     */
    private static List<ConstrainedNode> clearNodes(List<ConstrainedNode> nodes) {
        List<ConstrainedNode> tmp = nodes.stream()
                .filter(node -> !node.getChildren().isEmpty() || !node.getConstraints().isEmpty())
                .collect(Collectors.toCollection(LinkedList::new));
        return CollectionUtils.unmodifiableList(tmp);
    }

    /**
     * Get indexes constraints map.
     *
     * @param annotatedType annotated type
     * @return indexes constraints map
     */
    public static Map<List<Short>, List<Annotation>> getIndexesConstraintsMap(
            AnnotatedType annotatedType) {
        return IndexesConstraintsUtils.resolve(annotatedType);
    }

    /** Indexes constraints utils. */
    @UtilityClass
    private static class IndexesConstraintsUtils {

        /** class: AnnotatedTypeBaseImpl */
        private static final Class<?> ANNOTATED_TYPE_BASE_IMPL = Objects.requireNonNull(ReflectionUtils
                .getClass("sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl"));
        /** field: AnnotatedTypeFactory$AnnotatedTypeBaseImpl#allOnSameTargetTypeAnnotations */
        private static final Field ALL_ON_SAME_TARGET_TYPE_ANNOTATIONS = Objects.requireNonNull(ReflectionUtils
                .accessField(
                        ReflectionUtils.getClass("sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl"),
                        "allOnSameTargetTypeAnnotations"));
        /** field: TypeAnnotation#annotation */
        private static final Field ANNOTATION_OF_TYPE_ANNOTATION = Objects.requireNonNull(ReflectionUtils
                .accessField(ReflectionUtils.getClass("sun.reflect.annotation.TypeAnnotation"), "annotation"));
        /** field: TypeAnnotation#loc */
        private static final Field LOC_OF_TYPE_ANNOTATION = Objects.requireNonNull(ReflectionUtils
                .accessField(ReflectionUtils.getClass("sun.reflect.annotation.TypeAnnotation"), "loc"));
        /** field: TypeAnnotation$LocationInfo#locations */
        private static final Field LOCATIONS_OF_LOCATION_INFO = Objects.requireNonNull(ReflectionUtils
                .accessField(
                        ReflectionUtils.getClass("sun.reflect.annotation.TypeAnnotation$LocationInfo"), "locations"));
        /** field: TypeAnnotation$LocationInfo$Location#index */
        private static final Field INDEX_OF_LOCATION = Objects.requireNonNull(ReflectionUtils
                .accessField(
                        ReflectionUtils.getClass("sun.reflect.annotation.TypeAnnotation$LocationInfo$Location"),
                        "index"));

        /**
         * Get indexes constraints map.
         *
         * @param annotatedType an annotated type
         * @return indexes constraints map of the annotated type
         */
        public static Map<List<Short>, List<Annotation>> resolve(AnnotatedType annotatedType) {
            if (annotatedType.getClass() == ANNOTATED_TYPE_BASE_IMPL) {
                return Collections.singletonMap(
                        Collections.emptyList(),
                        Arrays.stream(annotatedType.getAnnotations())
                                .filter(ConstraintUtils::isConstraint)
                                .collect(Collectors.toCollection(LinkedList::new)));
            }
            Map<List<Short>, List<Annotation>> indexesConstraintsMap = new LinkedHashMap<>();
            Object allOnSameTargetTypeAnnotations = ReflectionUtils.getFieldValue(
                    annotatedType, ALL_ON_SAME_TARGET_TYPE_ANNOTATIONS);
            for (int i = 0, l = Array.getLength(allOnSameTargetTypeAnnotations); i < l; i++) {
                Object typeAnnotation = Array.get(allOnSameTargetTypeAnnotations, i);
                processTypeAnnotation(indexesConstraintsMap, typeAnnotation);
            }
            indexesConstraintsMap.entrySet().forEach(e -> e.setValue(CollectionUtils.unmodifiableList(e.getValue())));
            return indexesConstraintsMap;
        }

        /**
         * Process type annotation.
         *
         * @param indexesConstraintsMap an indexes constraints map
         * @param typeAnnotation type annotation
         */
        private static void processTypeAnnotation(
                Map<List<Short>, List<Annotation>> indexesConstraintsMap, Object typeAnnotation) {
            Annotation annotation = ReflectionUtils.getFieldValue(typeAnnotation, ANNOTATION_OF_TYPE_ANNOTATION);
            if (!ConstraintUtils.isConstraint(annotation)) { return; }
            Object loc = ReflectionUtils.getFieldValue(typeAnnotation, LOC_OF_TYPE_ANNOTATION);
            Object locations = ReflectionUtils.getFieldValue(loc, LOCATIONS_OF_LOCATION_INFO);
            int locationsLength = Array.getLength(locations);
            List<Short> indexes = new ArrayList<>(locationsLength);
            for (int j = 0; j < locationsLength; j++) {
                Object location = Array.get(locations, j);
                Short index = ReflectionUtils.getFieldValue(location, INDEX_OF_LOCATION);
                indexes.add(index);
            }
            indexesConstraintsMap.computeIfAbsent(indexes, ignored -> new LinkedList<>()).add(annotation);
        }

    }

}
