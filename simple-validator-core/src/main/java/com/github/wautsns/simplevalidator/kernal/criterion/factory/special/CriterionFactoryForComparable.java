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

import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactoryForNonPrimitive;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Criterion factory for {@code Comparable} type.
 *
 * @author wautsns
 * @since Mar 15, 2020
 */
public abstract class CriterionFactoryForComparable<A extends Annotation, T>
        extends CriterionFactoryForNonPrimitive<A, Comparable<T>> {

    @Override
    public boolean applyTo(Type type, A constraint) {
        return TypeUtils.isAssignableTo(type, Comparable.class);
    }

    // #################### utils #######################################################

    /**
     * Return whether valueA is equal to valueB.
     *
     * @param valueA valueA
     * @param valueB valueB
     * @param <T> type of value
     * @return {@code true} if valueA is equal to valueB, otherwise {@code false}
     */
    public static <T> boolean eq(Comparable<T> valueA, T valueB) {
        return (valueA.compareTo(valueB) == 0);
    }

    /**
     * Return whether valueA is not equal to valueB.
     *
     * @param valueA valueA
     * @param valueB valueB
     * @param <T> type of value
     * @return {@code true} if valueA is not equal to valueB, otherwise {@code false}
     */
    public static <T> boolean ne(Comparable<T> valueA, T valueB) {
        return (valueA.compareTo(valueB) != 0);
    }

    /**
     * Return whether valueA is greater than valueB.
     *
     * @param valueA valueA
     * @param valueB valueB
     * @param <T> type of value
     * @return {@code true} if valueA is greater than valueB, otherwise {@code false}
     */
    public static <T> boolean gt(Comparable<T> valueA, T valueB) {
        return (valueA.compareTo(valueB) > 0);
    }

    /**
     * Return whether valueA is greater than or equal to valueB.
     *
     * @param valueA valueA
     * @param valueB valueB
     * @param <T> type of value
     * @return {@code true} if valueA is greater than or equal to valueB, otherwise {@code false}
     */
    public static <T> boolean ge(Comparable<T> valueA, T valueB) {
        return (valueA.compareTo(valueB) >= 0);
    }

    /**
     * Return whether valueA is less than valueB.
     *
     * @param valueA valueA
     * @param valueB valueB
     * @param <T> type of value
     * @return {@code true} if valueA is less than valueB, otherwise {@code false}
     */
    public static <T> boolean lt(Comparable<T> valueA, T valueB) {
        return (valueA.compareTo(valueB) < 0);
    }

    /**
     * Return whether valueA is less than or equal to valueB.
     *
     * @param valueA valueA
     * @param valueB valueB
     * @param <T> type of value
     * @return {@code true} if valueA is less than or equal to valueB, otherwise {@code false}
     */
    public static <T> boolean le(Comparable<T> valueA, T valueB) {
        return (valueA.compareTo(valueB) <= 0);
    }

}
