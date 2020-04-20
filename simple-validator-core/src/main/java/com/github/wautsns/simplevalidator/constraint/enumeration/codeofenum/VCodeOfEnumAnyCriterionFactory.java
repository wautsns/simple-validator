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

import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.special.NonPrimitiveCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
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
 * @author wautsns
 * @since Mar 11, 2020
 */
@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VCodeOfEnumAnyCriterionFactory extends NonPrimitiveCriterionFactory<VCodeOfEnum> {

    /** {@code VCodeOfEnumAnyCriterionFactory} instance */
    public static final VCodeOfEnumAnyCriterionFactory INSTANCE = new VCodeOfEnumAnyCriterionFactory();

    @Override
    public void process(ConstrainedNode node, VCodeOfEnum constraint, TCriteria<Object> wip) {
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
    protected static <T extends Enum<T> & CodableEnum<E>, E> TCriterion<Object> produce(VCodeOfEnum constraint) {
        Class<T> clazz = (Class<T>) constraint.value();
        String[] include = constraint.include();
        String[] exclude = constraint.exclude();
        if (include.length == 0 && exclude.length == 0) {
            return CACHE.computeIfAbsent(clazz, VCodeOfEnumAnyCriterionFactory::initForAllEnums);
        } else {
            return initForSpecifiedEnums(clazz, include, exclude);
        }
    }

    /** codable enumeration type -> criterion cache */
    private static final Map<Class<?>, TCriterion<Object>> CACHE = new ConcurrentHashMap<>();

    /**
     * Initialize a criterion for all enumerations.
     *
     * @param clazz codable enumeration class
     * @param <T> type of enumeration code
     * @param <E> type of enumeration
     * @return criterion for all values
     */
    @SuppressWarnings("rawtypes")
    private static <T extends Enum<T> & CodableEnum<E>, E> TCriterion<Object> initForAllEnums(Class<?> clazz) {
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
    private static <T extends Enum<T> & CodableEnum<E>, E> TCriterion<Object> initForSpecifiedEnums(
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
    private static <T extends Enum<T> & CodableEnum<E>, E> TCriterion<Object> initForCodes(List<T> codes) {
        Object[] optionalValues = codes.stream()
                // Don't replace with method reference!! (will cause LambdaConversionException)
                .map(codableEnum -> codableEnum.getCode())
                .toArray();
        return value -> {
            for (T codableEnum : codes) {
                if (codableEnum.equalsToCode((E) value)) {
                    return null;
                }
            }
            return new ValidationFailure(value).put(VCodeOfEnum.OPTIONAL_VALUES, optionalValues);
        };
    }

}
