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
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForCharSequence;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * VNameOfEnum criterion factory for {@code CharSequence} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VNameOfEnumCriterionFactoryForCharSequence extends CriterionFactoryForCharSequence<VNameOfEnum> {

    /** {@code VNameOfEnumCriterionFactoryForCharSequence} instance. */
    public static final VNameOfEnumCriterionFactoryForCharSequence INSTANCE = new VNameOfEnumCriterionFactoryForCharSequence();

    @Override
    public void process(ConstrainedNode node, VNameOfEnum constraint, CriteriaForNonPrimitive<CharSequence> wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @return criterion
     */
    protected static CriterionForNonPrimitive<CharSequence> produce(VNameOfEnum constraint) {
        Class<?> clazz = constraint.value();
        String[] include = constraint.include();
        String[] exclude = constraint.exclude();
        if (include.length == 0 && exclude.length == 0) {
            return CACHE.computeIfAbsent(clazz, VNameOfEnumCriterionFactoryForCharSequence::initForAllEnums);
        } else {
            return initForSpecifiedEnums(clazz, include, exclude);
        }
    }

    /** Enumeration type -> criterion cache. */
    private static final Map<Class<?>, CriterionForNonPrimitive<CharSequence>> CACHE = new ConcurrentHashMap<>(8);

    /**
     * Initialize a criterion for all enumerations.
     *
     * @param clazz codable enumeration class
     * @return criterion for all values
     */
    private static CriterionForNonPrimitive<CharSequence> initForAllEnums(Class<?> clazz) {
        Enum<?>[] enums = (Enum<?>[]) clazz.getEnumConstants();
        return initForNames(Arrays.stream(enums).map(Enum::name).toArray(String[]::new));
    }

    /**
     * Initialize a criterion for the specified enumerations.
     *
     * @param clazz codable enumeration class
     * @return criterion for the specified values
     */
    private static CriterionForNonPrimitive<CharSequence> initForSpecifiedEnums(
            Class<?> clazz, String[] include, String[] exclude) {
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
    private static CriterionForNonPrimitive<CharSequence> initForNames(String[] names) {
        return new CriterionForNonPrimitive<CharSequence>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(CharSequence value) {
                if (value != null && value.length() > 0) {
                    for (String name : names) {
                        if (name.contentEquals(value)) {
                            return null;
                        }
                    }
                }
                return new ValidationFailure(value).put(VCodeOfEnum.OPTIONAL_VALUES, names);
            }
        };
    }

}
