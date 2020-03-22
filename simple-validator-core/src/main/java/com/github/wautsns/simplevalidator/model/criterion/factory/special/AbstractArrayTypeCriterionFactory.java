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
package com.github.wautsns.simplevalidator.model.criterion.factory.special;

import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.factory.TCriterionFactory;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Abstract array type criterion factory.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class AbstractArrayTypeCriterionFactory<A extends Annotation>
        implements TCriterionFactory<A, Object> {

    @Override
    public boolean appliesTo(Type type, A constraint) {
        return TypeUtils.isArray(type);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void process(ConstrainedNode node, A constraint, TCriteria<Object> wip) {
        Type componentType = TypeUtils.getComponentType(node.getType());
        if (!TypeUtils.isPrimitive(componentType)) {
            processTArray(node, constraint, (TCriteria) wip);
        } else if (componentType == int.class) {
            processIntArray(node, constraint, (TCriteria) wip);
        } else if (componentType == long.class) {
            processLongArray(node, constraint, (TCriteria) wip);
        } else if (componentType == boolean.class) {
            processBooleanArray(node, constraint, (TCriteria) wip);
        } else if (componentType == char.class) {
            processCharArray(node, constraint, (TCriteria) wip);
        } else if (componentType == byte.class) {
            processByteArray(node, constraint, (TCriteria) wip);
        } else if (componentType == double.class) {
            processDoubleArray(node, constraint, (TCriteria) wip);
        } else if (componentType == short.class) {
            processShortArray(node, constraint, (TCriteria) wip);
        } else if (componentType == float.class) {
            processFloatArray(node, constraint, (TCriteria) wip);
        }
    }

    /**
     * Process wip of criteria for T array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for T array
     */
    protected abstract <T> void processTArray(ConstrainedNode node, A constraint, TCriteria<T[]> wip);

    /**
     * Process wip of criteria for {@code int} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code int} array
     */
    protected abstract void processIntArray(ConstrainedNode node, A constraint, TCriteria<int[]> wip);

    /**
     * Process wip of criteria for {@code long} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code long} array
     */
    protected abstract void processLongArray(ConstrainedNode node, A constraint, TCriteria<long[]> wip);

    /**
     * Process wip of criteria for {@code boolean} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code boolean} array
     */
    protected abstract void processBooleanArray(ConstrainedNode node, A constraint, TCriteria<boolean[]> wip);

    /**
     * Process wip of criteria for {@code char} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code char} array
     */
    protected abstract void processCharArray(ConstrainedNode node, A constraint, TCriteria<char[]> wip);

    /**
     * Process wip of criteria for {@code byte} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code byte} array
     */
    protected abstract void processByteArray(ConstrainedNode node, A constraint, TCriteria<byte[]> wip);

    /**
     * Process wip of criteria for {@code double} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code double} array
     */
    protected abstract void processDoubleArray(ConstrainedNode node, A constraint, TCriteria<double[]> wip);

    /**
     * Process wip of criteria for {@code short} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code short} array
     */
    protected abstract void processShortArray(ConstrainedNode node, A constraint, TCriteria<short[]> wip);

    /**
     * Process wip of criteria for {@code float} array.
     *
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria for {@code float} array
     */
    protected abstract void processFloatArray(ConstrainedNode node, A constraint, TCriteria<float[]> wip);

}
