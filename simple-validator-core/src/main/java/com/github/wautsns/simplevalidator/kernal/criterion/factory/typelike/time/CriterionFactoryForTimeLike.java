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
package com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time;

import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.CriterionFactoryForTypeLike;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.utility.TimeLikeUtilityForCalendar;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.utility.TimeLikeUtilityForDate;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.utility.TimeLikeUtilityForInstant;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.utility.TimeLikeUtilityForLocalDate;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.utility.TimeLikeUtilityForLocalDateTime;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.utility.TimeLikeUtilityForLocalTime;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.utility.TimeLikeUtilityForLong;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Criterion factory for time-like.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class CriterionFactoryForTimeLike<A extends Annotation>
        extends CriterionFactoryForTypeLike<A, TimeLikeUtility<?>> {

    /** Default time-like utilities. */
    public static final List<TimeLikeUtility<?>> UTILITIES = new LinkedList<>(Arrays.asList(
            TimeLikeUtilityForCalendar.DEFAULT,
            TimeLikeUtilityForDate.DEFAULT,
            TimeLikeUtilityForInstant.DEFAULT,
            TimeLikeUtilityForLocalDateTime.DEFAULT,
            TimeLikeUtilityForLocalDate.DEFAULT,
            TimeLikeUtilityForLocalTime.DEFAULT,
            TimeLikeUtilityForLong.DEFAULT));

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public final void process(ConstrainedNode node, A constraint, CriteriaForNonPrimitive<Object> wip) {
        TimeLikeUtility utility = requireTypeLikeUtility(node.getType());
        process(utility, node, constraint, wip);
    }

    /**
     * Process wip of criteria.
     *
     * @param utility time-like utility
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria
     * @param <T> type of time
     */
    protected abstract <T> void process(
            TimeLikeUtility<T> utility, ConstrainedNode node, A constraint, CriteriaForNonPrimitive<T> wip);

    @Override
    protected List<TimeLikeUtility<?>> initTypeLikeUtilities() {
        return UTILITIES;
    }

}
