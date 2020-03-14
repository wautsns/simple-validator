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

import com.github.wautsns.simplevalidator.model.criterion.factory.TCriterionFactory;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;

import java.lang.annotation.Annotation;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class AbstractComparableNumberCriterionFactory<A extends Annotation>
        implements TCriterionFactory<A, Comparable<Number>> {

    @Override
    public boolean appliesTo(ConstrainedNode node, A constraint) {
        return TypeUtils.isAssignableToAll(node.getType(), Number.class, Comparable.class);
    }

    // ------------------------- utils ---------------------------------------------

    public static <T> boolean equalTo(Comparable<T> valueA, T valueB) {
        return valueA.compareTo(valueB) == 0;
    }

    public static <T> boolean notEqualTo(Comparable<T> valueA, T valueB) {
        return valueA.compareTo(valueB) != 0;
    }

    public static <T> boolean greaterThan(Comparable<T> valueA, T valueB) {
        return valueA.compareTo(valueB) > 0;
    }

    public static <T> boolean greaterThanOrEqualTo(Comparable<T> valueA, T valueB) {
        return valueA.compareTo(valueB) >= 0;
    }

    public static <T> boolean lessThan(Comparable<T> valueA, T valueB) {
        return valueA.compareTo(valueB) < 0;
    }

    public static <T> boolean lessThanOrEqualTo(Comparable<T> valueA, T valueB) {
        return valueA.compareTo(valueB) <= 0;
    }

}
