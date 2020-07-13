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
package com.github.wautsns.simplevalidator.constraint.enumeration.nameofenum;

import com.github.wautsns.simplevalidator.constraint.enumeration.codeofenum.VCodeOfEnum;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.TCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VNameOfEnumCharSequenceCriterionFactory implements TCriterionFactory<VNameOfEnum, CharSequence> {

    /** {@code VNameOfEnumCharSequenceCriterionFactory} instance */
    public static final VNameOfEnumCharSequenceCriterionFactory INSTANCE = new VNameOfEnumCharSequenceCriterionFactory();

    @Override
    public boolean appliesTo(Type type, VNameOfEnum constraint) {
        return TypeUtils.isAssignableTo(type, CharSequence.class);
    }

    @Override
    public void process(ConstrainedNode node, VNameOfEnum constraint, TCriteria<CharSequence> wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @return criterion
     */
    protected static TCriterion<CharSequence> produce(VNameOfEnum constraint) {
        Class<?> clazz = constraint.value();
        String[] include = constraint.include();
        String[] exclude = constraint.exclude();
        if (include.length == 0 && exclude.length == 0) {
            return CACHE.computeIfAbsent(clazz, VNameOfEnumCharSequenceCriterionFactory::initForAllEnums);
        } else {
            return initForSpecifiedEnums(clazz, include, exclude);
        }
    }

    /** Enumeration type -> criterion cache. */
    private static final Map<Class<?>, TCriterion<CharSequence>> CACHE = new ConcurrentHashMap<>();

    /**
     * Initialize a criterion for all enumerations.
     *
     * @param clazz codable enumeration class
     * @return criterion for all values
     */
    private static TCriterion<CharSequence> initForAllEnums(Class<?> clazz) {
        Enum<?>[] enums = (Enum<?>[]) clazz.getEnumConstants();
        return initForNames(Arrays.stream(enums).map(Enum::name).toArray(String[]::new));
    }

    /**
     * Initialize a criterion for the specified enumerations.
     *
     * @param clazz codable enumeration class
     * @return criterion for the specified values
     */
    private static TCriterion<CharSequence> initForSpecifiedEnums(Class<?> clazz, String[] include, String[] exclude) {
        Enum<?>[] enums = (Enum<?>[]) clazz.getEnumConstants();
        Stream<String> optionalValuesStream = Arrays.stream(enums).map(Enum::name);
        if (include.length > 0) {
            Set<String> namesIncluded = new HashSet<>(Arrays.asList(include));
            optionalValuesStream = optionalValuesStream.filter(namesIncluded::contains);
        }
        if (exclude.length > 0) {
            Set<String> namesExcluded = new HashSet<>(Arrays.asList(exclude));
            optionalValuesStream = optionalValuesStream.filter(name -> !namesExcluded.contains(name));
        }
        String[] optionalValues = optionalValuesStream.toArray(String[]::new);
        return initForNames(optionalValues);
    }

    /**
     * Initialize a criterion for the specified names.
     *
     * @param names names
     * @return criterion for the specified names
     */
    private static TCriterion<CharSequence> initForNames(String[] names) {
        return value -> {
            if (value != null && value.length() > 0) {
                for (String name : names) {
                    if (name.contentEquals(value)) {
                        return null;
                    }
                }
            }
            return new ValidationFailure(value).put(VCodeOfEnum.OPTIONAL_VALUES, names);
        };
    }

}
