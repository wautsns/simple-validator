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
package com.github.wautsns.simplevalidator.kernal.criterion.criteria;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.kernal.criterion.util.CriterionUtils;

import java.util.List;

/**
 * Criteria.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
public interface Criteria<C extends Criterion> {

    /** Set the criteria support {@code null} value. */
    void nullable();

    /**
     * Return whether the criteria support {@code null} value.
     *
     * @return {@code true} if the criteria support {@code null} value, otherwise {@code false}
     */
    boolean isNullable();

    /**
     * Get original criteria.
     *
     * @return original criteria
     */
    List<C> getOriginalCriteria();

    /**
     * Add criterion.
     *
     * @param criterion criterion
     */
    default void add(C criterion) {
        if (criterion != null) { getOriginalCriteria().add(criterion); }
    }

    /**
     * Simplify the criteria.
     *
     * @return criterion after simplifying
     */
    @SuppressWarnings("unchecked")
    default C simplify() {
        List<C> originalCriteria = getOriginalCriteria();
        for (int index = 0; index < originalCriteria.size(); ) {
            C criterion = originalCriteria.get(index);
            if (criterion instanceof Criteria) {
                Criteria<C> criteria = (Criteria<C>) criterion;
                originalCriteria.remove(index);
                originalCriteria.addAll(index, criteria.getOriginalCriteria());
            } else if (CriterionUtils.isTheTruth(criterion)) {
                originalCriteria.remove(index);
            } else {
                index++;
            }
        }
        if (originalCriteria.isEmpty()) {
            return CriterionUtils.getTheTruth((Class<C>) getClass());
        } else if (originalCriteria.size() == 1) {
            return originalCriteria.get(0);
        } else {
            return (C) this;
        }
    }

}
