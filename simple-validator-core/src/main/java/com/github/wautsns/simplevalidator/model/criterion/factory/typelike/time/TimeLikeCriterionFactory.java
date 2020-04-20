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
package com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time;

import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.TypeLikeCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.utility.CalendarUtility;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.utility.DateUtility;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.utility.InstantUtility;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.utility.LocalDateTimeUtility;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.utility.LocalDateUtility;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.utility.LocalTimeUtility;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.utility.LongUtility;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

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
public abstract class TimeLikeCriterionFactory<A extends Annotation>
        extends TypeLikeCriterionFactory<A, TimeLikeUtility<?>> {

    /** default time like utility */
    public static final List<TimeLikeUtility<?>> DEFAULT_UTILITIES = new LinkedList<>(Arrays.asList(
            CalendarUtility.DEFAULT,
            DateUtility.DEFAULT,
            InstantUtility.DEFAULT,
            LocalDateTimeUtility.DEFAULT,
            LocalDateUtility.DEFAULT,
            LocalTimeUtility.DEFAULT,
            LongUtility.DEFAULT));

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public final void process(ConstrainedNode node, A constraint, TCriteria<Object> wip) {
        TimeLikeUtility utility = getTypeLikeUtility(node.getType());
        process(utility, node, constraint, wip);
    }

    /**
     * Process wip of criteria.
     *
     * @param utility time like utility
     * @param node constrained node
     * @param constraint constraint
     * @param wip wip of criteria
     */
    protected abstract <T> void process(
            TimeLikeUtility<T> utility,
            ConstrainedNode node, A constraint, TCriteria<T> wip);

    @Override
    protected List<TimeLikeUtility<?>> initTypeLikeUtilities() {
        return DEFAULT_UTILITIES;
    }

}
