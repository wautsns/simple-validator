/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.wautsns.simplevalidator.constraint.enumeration.codeofenum;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForAnyNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * VCodeOfEnum criterion factory for any type.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VCodeOfEnumCriterionFactoryForAnyType extends CriterionFactoryForAnyNonPrimitive<VCodeOfEnum> {

    /** {@code VCodeOfEnumCriterionFactoryForAnyType} instance. */
    public static final VCodeOfEnumCriterionFactoryForAnyType INSTANCE = new VCodeOfEnumCriterionFactoryForAnyType();

    @Override
    public void process(ConstrainedNode node, VCodeOfEnum constraint, CriteriaForNonPrimitive<Object> wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @param <T> type of enumeration code
     * @param <E> type of enumeration
     * @return criterion
     */
    protected static <T extends Enum<T> & CodableEnum<E>, E> CriterionForNonPrimitive<Object> produce(
            VCodeOfEnum constraint) {
        Class<T> clazz = (Class<T>) constraint.value();
        String[] include = constraint.include();
        String[] exclude = constraint.exclude();
        if (include.length == 0 && exclude.length == 0) {
            return CACHE.computeIfAbsent(clazz, VCodeOfEnumCriterionFactoryForAnyType::initForAllEnums);
        } else {
            return initForSpecifiedEnums(clazz, include, exclude);
        }
    }

    /** Codable enumeration type -> criterion cache. */
    private static final Map<Class<?>, CriterionForNonPrimitive<Object>> CACHE = new ConcurrentHashMap<>(8);

    /**
     * Initialize a criterion for all enumerations.
     *
     * @param clazz codable enumeration class
     * @param <T> type of enumeration code
     * @param <E> type of enumeration
     * @return criterion for all values
     */
    @SuppressWarnings("rawtypes")
    private static <T extends Enum<T> & CodableEnum<E>, E> CriterionForNonPrimitive<Object> initForAllEnums(
            Class<?> clazz) {
        Object[] enums = clazz.getEnumConstants();
        return initForCodes((List) Arrays.asList(enums));
    }

    /**
     * Initialize a criterion for the specified enumerations.
     *
     * @param clazz codable enumeration class
     * @param <T> type of enumeration code
     * @param <E> type of enumeration
     * @return criterion for the specified values
     */
    private static <T extends Enum<T> & CodableEnum<E>, E> CriterionForNonPrimitive<Object> initForSpecifiedEnums(
            Class<T> clazz, String[] include, String[] exclude) {
        Stream<T> stream;
        if (include.length == 0) {
            stream = Arrays.stream(clazz.getEnumConstants());
        } else {
            stream = Arrays.stream(include).map(name -> Enum.valueOf(clazz, name));
        }
        if (exclude.length > 0) {
            Set<String> namesExcluded = new HashSet<>(Arrays.asList(exclude));
            stream = stream.filter(codableEnum -> !namesExcluded.contains(codableEnum.name()));
        }
        return initForCodes(stream.collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     * Initialize a criterion for the specified codes.
     *
     * @param codes codes
     * @param <T> type of enumeration code
     * @param <E> type of enumeration
     * @return criterion for the specified codes
     */
    private static <T extends Enum<T> & CodableEnum<E>, E> CriterionForNonPrimitive<Object> initForCodes(
            List<T> codes) {
        Object[] optionalValues = codes.stream()
                // Don't replace with method reference!! (will cause LambdaConversionException)
                .map(codableEnum -> codableEnum.getCode())
                .toArray();
        return new CriterionForNonPrimitive<Object>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Object value) {
                for (T codableEnum : codes) {
                    if (codableEnum.equalToCode((E) value)) {
                        return null;
                    }
                }
                return new ValidationFailure(value).put(VCodeOfEnum.OPTIONAL_VALUES, optionalValues);
            }
        };
    }

}
