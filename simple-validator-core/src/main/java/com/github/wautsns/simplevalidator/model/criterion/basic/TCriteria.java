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

import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;

/**
 * Criteria for non-primitive value.
 *
 * @param <T> type of value
 * @author wautsns
 * @since Mar 11, 2020
 */
public class TCriteria<T> extends Criteria<TCriterion<? super T>> implements TCriterion<T> {

    /** whether the value can be {@code null} */
    private boolean nullable = false;

    /**
     * Return whether the value can be {@code null}.
     *
     * @return {@code true} if the value can be {@code null}, otherwise {@code false}
     */
    public boolean isNullable() {
        return nullable;
    }

    /** Set that the value can be {@code null} for the criteria. */
    public void nullable() {
        nullable = true;
    }

    @Override
    public ValidationFailure test(T value) {
        if ((value == null) && nullable) { return null; }
        for (TCriterion<? super T> criterion : criterionList) {
            ValidationFailure failure = criterion.test(value);
            if (failure != null) { return failure; }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TCriterion<T> simplify() {
        if (criterionList.isEmpty()) { return null; }
        int index = 0;
        while (index < criterionList.size()) {
            TCriterion<? super T> criterion = criterionList.get(index);
            if (criterion instanceof TCriteria) {
                TCriteria<T> criteria = (TCriteria<T>) criterion;
                if (!nullable && criteria.nullable) {
                    index++;
                } else {
                    criterionList.remove(index);
                    criterionList.addAll(index, criteria.criterionList);
                }
            } else {
                index++;
            }
        }
        if (nullable || (criterionList.size() > 1)) {
            return this;
        } else {
            return (TCriterion<T>) criterionList.get(0);
        }
    }

}
