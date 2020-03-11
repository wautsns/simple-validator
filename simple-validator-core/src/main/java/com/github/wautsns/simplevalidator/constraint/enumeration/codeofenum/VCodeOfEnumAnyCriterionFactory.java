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

import com.github.wautsns.simplevalidator.model.criterion.factory.special.AbstractNonPrimitiveCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriterion;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

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
public class VCodeOfEnumAnyCriterionFactory extends AbstractNonPrimitiveCriterionFactory<VCodeOfEnum> {

    @Override
    public void process(ConstrainedNode node, VCodeOfEnum constraint, TCriteria<Object> wip) {
        wip.add(produce(constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected static <T extends Enum<T> & CodableEnum<E>, E> TCriterion<Object> produce(VCodeOfEnum constraint) {
        Class<T> codableEnumClass = (Class<T>) constraint.value();
        String[] include = constraint.include();
        String[] exclude = constraint.exclude();
        if (include.length == 0 && exclude.length == 0) {
            return CACHE.computeIfAbsent(codableEnumClass, VCodeOfEnumAnyCriterionFactory::initAll);
        } else {
            return initSpecific(codableEnumClass, include, exclude);
        }
    }

    @SuppressWarnings("rawtypes")
    private static <T extends Enum<T> & CodableEnum<E>, E> TCriterion<Object> initAll(Class<?> codableEnumClass) {
        Object[] enumConstants = codableEnumClass.getEnumConstants();
        return init((List) Arrays.asList(enumConstants));
    }

    private static <T extends Enum<T> & CodableEnum<E>, E> TCriterion<Object> initSpecific(
            Class<T> codableEnumClass, String[] include, String[] exclude) {
        Stream<T> stream;
        if (include.length == 0) {
            stream = Arrays.stream(codableEnumClass.getEnumConstants());
        } else {
            stream = Arrays.stream(include).map(name -> Enum.valueOf(codableEnumClass, name));
        }
        if (exclude.length > 0) {
            Set<String> namesExcluded = new HashSet<>(Arrays.asList(exclude));
            stream = stream.filter(codableEnum -> !namesExcluded.contains(codableEnum.name()));
        }
        return init(stream.collect(Collectors.toCollection(LinkedList::new)));
    }

    private static <T extends Enum<T> & CodableEnum<E>, E> TCriterion<Object> init(
            List<T> codableEnums) {
        Object[] optionalValues = codableEnums.stream()
                // Don't convert to method reference!! (will cause LambdaConversionException)
                .map(codableEnum -> codableEnum.getCode())
                .toArray();
        return value -> {
            for (T codableEnum : codableEnums) {
                if (codableEnum.equalToCode((E) value)) {
                    return null;
                }
            }
            return new ValidationFailure(value).put(VCodeOfEnum.OPTIONAL_VALUES, optionalValues);
        };
    }

    private static final Map<Class<?>, TCriterion<Object>> CACHE = new ConcurrentHashMap<>();

}
