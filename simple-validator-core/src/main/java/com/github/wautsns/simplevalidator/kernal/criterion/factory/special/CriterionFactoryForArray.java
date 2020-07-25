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
package com.github.wautsns.simplevalidator.kernal.criterion.factory.special;

import com.github.wautsns.simplevalidator.exception.analysis.IllegalConstrainedNodeException;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactoryForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Criterion factory for array type.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class CriterionFactoryForArray<A extends Annotation>
        extends CriterionFactoryForNonPrimitive<A, Object> {

    @Override
    public final boolean applyTo(Type type, A constraint) {
        return TypeUtils.isArray(type);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked", "UnnecessaryLocalVariable" })
    public final void process(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<Object> wip) {
        Type componentType = TypeUtils.getComponentType(node.getType());
        CriteriaForNonPrimitive tmpWip = wip;
        if (!TypeUtils.isPrimitive(componentType)) {
            processTArray(node, constraint, tmpWip);
        } else if (componentType == int.class) {
            processIntArray(node, constraint, tmpWip);
        } else if (componentType == long.class) {
            processLongArray(node, constraint, tmpWip);
        } else if (componentType == boolean.class) {
            processBooleanArray(node, constraint, tmpWip);
        } else if (componentType == char.class) {
            processCharArray(node, constraint, tmpWip);
        } else if (componentType == byte.class) {
            processByteArray(node, constraint, tmpWip);
        } else if (componentType == double.class) {
            processDoubleArray(node, constraint, tmpWip);
        } else if (componentType == short.class) {
            processShortArray(node, constraint, tmpWip);
        } else if (componentType == float.class) {
            processFloatArray(node, constraint, tmpWip);
        }
    }

    /**
     * Process wip of criteria for T array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for T array
     * @param <T> type of array element
     */
    protected <T> void processTArray(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<T[]> wip) {
        throw new IllegalConstrainedNodeException(node.getLocation(), constraint);
    }

    /**
     * Process wip of criteria for {@code int} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code int} array
     */
    protected void processIntArray(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<int[]> wip) {
        throw new IllegalConstrainedNodeException(node.getLocation(), constraint);
    }

    /**
     * Process wip of criteria for {@code long} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code long} array
     */
    protected void processLongArray(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<long[]> wip) {
        throw new IllegalConstrainedNodeException(node.getLocation(), constraint);
    }

    /**
     * Process wip of criteria for {@code boolean} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code boolean} array
     */
    protected void processBooleanArray(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<boolean[]> wip) {
        throw new IllegalConstrainedNodeException(node.getLocation(), constraint);
    }

    /**
     * Process wip of criteria for {@code char} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code char} array
     */
    protected void processCharArray(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<char[]> wip) {
        throw new IllegalConstrainedNodeException(node.getLocation(), constraint);
    }

    /**
     * Process wip of criteria for {@code byte} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code byte} array
     */
    protected void processByteArray(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<byte[]> wip) {
        throw new IllegalConstrainedNodeException(node.getLocation(), constraint);
    }

    /**
     * Process wip of criteria for {@code double} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code double} array
     */
    protected void processDoubleArray(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<double[]> wip) {
        throw new IllegalConstrainedNodeException(node.getLocation(), constraint);
    }

    /**
     * Process wip of criteria for {@code short} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code short} array
     */
    protected void processShortArray(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<short[]> wip) {
        throw new IllegalConstrainedNodeException(node.getLocation(), constraint);
    }

    /**
     * Process wip of criteria for {@code float} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code float} array
     */
    protected void processFloatArray(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<float[]> wip) {
        throw new IllegalConstrainedNodeException(node.getLocation(), constraint);
    }

}
