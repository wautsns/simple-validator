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

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.TypeLikeUtilityCriterionCache;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.CriterionFactoryForTimeLike;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time.TimeLikeUtility;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VPastCriterionFactoryForTimeLike extends CriterionFactoryForTimeLike<VPast> {

    public static final VPastCriterionFactoryForTimeLike INSTANCE = new VPastCriterionFactoryForTimeLike();

    @Override
    protected <T> void process(
            TimeLikeUtility<T> utility,
            ConstrainedNode node, VPast constraint, CriteriaForNonPrimitive<T> wip) {
        wip.add(initCriterion(utility, constraint));
    }

    // #################### criterion ###################################################

    protected static <T> CriterionForNonPrimitive<T> initCriterion(TimeLikeUtility<T> utility, VPast constraint) {
        long year = constraint.years();
        long month = constraint.months();
        long milliseconds = constraint.milliseconds() + TimeLikeUtility.toMilliseconds(
                constraint.weeks(), constraint.days(), constraint.hours(), constraint.minutes(), constraint.seconds());
        if ((year | month) != 0) {
            return initWithYearMonthMillisecondsOffset(utility, year, month, milliseconds);
        } else if (milliseconds == 0) {
            return initWithMillisecondsOffset(utility, milliseconds);
        } else {
            return CACHE_NO_OFFSET.get(utility);
        }
    }

    private static final TypeLikeUtilityCriterionCache<TimeLikeUtility<?>> CACHE_NO_OFFSET =
            new TypeLikeUtilityCriterionCache<>(VPastCriterionFactoryForTimeLike::initWithNoOffset);

    private static <T> CriterionForNonPrimitive<T> initWithNoOffset(TimeLikeUtility<T> utility) {
        return new CriterionForNonPrimitive<T>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(T value) {
                T ref = utility.now();
                return utility.isBefore(value, ref) ? null : wrong(utility, value, ref);
            }
        };
    }

    private static <T> CriterionForNonPrimitive<T> initWithMillisecondsOffset(
            TimeLikeUtility<T> utility, long milliseconds) {
        return new CriterionForNonPrimitive<T>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(T value) {
                T ref = utility.now(milliseconds);
                return utility.isBefore(value, ref) ? null : wrong(utility, value, ref);
            }
        };
    }

    private static <T> CriterionForNonPrimitive<T> initWithYearMonthMillisecondsOffset(
            TimeLikeUtility<T> utility, long years, long months, long milliseconds) {
        return new CriterionForNonPrimitive<T>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(T time) {
                T ref = utility.now(years, months, milliseconds);
                return utility.isBefore(time, ref) ? null : wrong(utility, time, ref);
            }
        };
    }

    protected static <T> ValidationFailure wrong(TimeLikeUtility<T> utility, T value, T ref) {
        return utility.fail(value).put(VPast.REF.get(utility), ref);
    }

}
