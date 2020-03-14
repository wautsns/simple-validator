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
package com.github.wautsns.simplevalidator.constraint.time.past;

import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.TypeLikeUtilityCriterionCache;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.AbstractTimeLikeCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.TimeLikeUtility;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VPastTimeLikeCriterionFactory extends AbstractTimeLikeCriterionFactory<VPast> {

    @Override
    protected <T> void process(
            TimeLikeUtility<T> utility,
            ConstrainedNode node, VPast constraint, TCriteria<T> wip) {
        wip.add(initCriterion(utility, constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected static <T> TCriterion<T> initCriterion(TimeLikeUtility<T> utility, VPast constraint) {
        long year = constraint.years();
        long month = constraint.months();
        long milliseconds = constraint.milliseconds() + TimeLikeUtility.toMilliseconds(
                constraint.days(), constraint.hours(), constraint.minutes(), constraint.seconds());
        if ((year | month) != 0) {
            return initWithYearMonthMillisecondsOffset(utility, year, month, milliseconds);
        } else if (milliseconds == 0) {
            return initWithMillisecondsOffset(utility, milliseconds);
        } else {
            return CACHE_NO_OFFSET.get(utility);
        }
    }

    private static final TypeLikeUtilityCriterionCache<TimeLikeUtility<?>> CACHE_NO_OFFSET =
            new TypeLikeUtilityCriterionCache<>(VPastTimeLikeCriterionFactory::initWithNoOffset);

    private static <T> TCriterion<T> initWithNoOffset(TimeLikeUtility<T> utility) {
        return time -> {
            T ref = utility.now();
            return utility.isBefore(time, ref) ? null : wrong(utility, time, ref);
        };
    }

    private static <T> TCriterion<T> initWithMillisecondsOffset(
            TimeLikeUtility<T> utility, long milliseconds) {
        return time -> {
            T ref = utility.now(milliseconds);
            return utility.isBefore(time, ref) ? null : wrong(utility, time, ref);
        };
    }

    private static <T> TCriterion<T> initWithYearMonthMillisecondsOffset(
            TimeLikeUtility<T> utility, long years, long months, long milliseconds) {
        return time -> {
            T ref = utility.now(years, months, milliseconds);
            return utility.isBefore(time, ref) ? null : wrong(utility, time, ref);
        };
    }

    // ------------------------- wrong ---------------------------------------------

    protected static <T> ValidationFailure wrong(TimeLikeUtility<T> utility, T value, T ref) {
        return utility.fail(value).put(VPast.REF.get(utility), ref);
    }

}
