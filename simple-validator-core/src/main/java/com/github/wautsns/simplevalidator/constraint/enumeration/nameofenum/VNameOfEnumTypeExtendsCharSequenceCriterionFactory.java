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
public class VNameOfEnumTypeExtendsCharSequenceCriterionFactory
        implements TCriterionFactory<VNameOfEnum, CharSequence> {

    @Override
    public boolean appliesTo(Type type, VNameOfEnum constraint) {
        return TypeUtils.isAssignableTo(type, CharSequence.class);
    }

    @Override
    public void process(ConstrainedNode node, VNameOfEnum constraint, TCriteria<CharSequence> wip) {
        wip.add(produce(constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected static TCriterion<CharSequence> produce(VNameOfEnum constraint) {
        Class<?> enumClass = constraint.value();
        String[] include = constraint.include();
        String[] exclude = constraint.exclude();
        if (include.length == 0 && exclude.length == 0) {
            return CACHE.computeIfAbsent(enumClass, VNameOfEnumTypeExtendsCharSequenceCriterionFactory::initAll);
        } else {
            return initSpecific(enumClass, include, exclude);
        }
    }

    private static TCriterion<CharSequence> initAll(Class<?> enumClass) {
        Enum<?>[] enums = (Enum<?>[]) enumClass.getEnumConstants();
        return init(Arrays.stream(enums).map(Enum::name).toArray(String[]::new));
    }

    private static TCriterion<CharSequence> initSpecific(Class<?> enumClass, String[] include, String[] exclude) {
        Enum<?>[] enums = (Enum<?>[]) enumClass.getEnumConstants();
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
        return init(optionalValues);
    }

    private static TCriterion<CharSequence> init(String[] optionalValues) {
        return value -> {
            if (value != null && value.length() > 0) {
                for (String optionalValue : optionalValues) {
                    if (optionalValue.contentEquals(value)) {
                        return null;
                    }
                }
            }
            return new ValidationFailure(value).put(VCodeOfEnum.OPTIONAL_VALUES, optionalValues);
        };
    }

    private static final Map<Class<?>, TCriterion<CharSequence>> CACHE = new ConcurrentHashMap<>();

}
