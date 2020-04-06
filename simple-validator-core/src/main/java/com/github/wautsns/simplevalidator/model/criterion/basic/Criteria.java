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
package com.github.wautsns.simplevalidator.model.criterion.basic;

import com.github.wautsns.simplevalidator.util.common.TypeUtils;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Criteria.
 *
 * @param <C> type of {@code Criterion}
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class Criteria<C extends Criterion> implements Criterion {

    /** criterionList */
    protected final List<C> criterionList = new LinkedList<>();

    /**
     * Add a criterion to the criteria.
     *
     * <p>{@code null} will not be added to criteria.
     *
     * @param criterion criterion
     */
    public void add(C criterion) {
        if (criterion != null) { criterionList.add(criterion); }
    }

    /**
     * Simplify the criteria.
     *
     * @return criterion after simplifying, or {@code null} if the criterion is unnecessary
     */
    @SuppressWarnings("unchecked")
    public C simplify() {
        if (criterionList.isEmpty()) { return null; }
        int index = 0;
        while (index < criterionList.size()) {
            C criterion = criterionList.get(index);
            if (criterion instanceof Criteria) {
                Criteria<C> criteria = (Criteria<C>) criterion;
                criterionList.remove(index);
                criterionList.addAll(index, criteria.criterionList);
            } else {
                index++;
            }
        }
        return (criterionList.size() > 1) ? (C) this : criterionList.get(0);
    }

    /**
     * New criteria for the specified type.
     *
     * @param type type of value
     * @return criteria for the specified type.
     */
    public static Criteria<?> newInstance(Type type) {
        if (!TypeUtils.isPrimitive(type)) {
            return new TCriteria<>();
        } else if (type == int.class) {
            return new IntCriteria();
        } else if (type == long.class) {
            return new LongCriteria();
        } else if (type == boolean.class) {
            return new BooleanCriteria();
        } else if (type == char.class) {
            return new CharCriteria();
        } else if (type == byte.class) {
            return new ByteCriteria();
        } else if (type == double.class) {
            return new DoubleCriteria();
        } else if (type == short.class) {
            return new ShortCriteria();
        } else if (type == float.class) {
            return new FloatCriteria();
        } else {
            throw new IllegalStateException();
        }
    }

}
