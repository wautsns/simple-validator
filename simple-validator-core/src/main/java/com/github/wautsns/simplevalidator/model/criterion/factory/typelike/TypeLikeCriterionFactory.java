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
package com.github.wautsns.simplevalidator.model.criterion.factory.typelike;

import com.github.wautsns.simplevalidator.exception.analysis.ConstraintAnalysisException;
import com.github.wautsns.simplevalidator.model.criterion.factory.TCriterionFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Criterion factory for type-like(e.g. text-like, time-like) value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class TypeLikeCriterionFactory<A extends Annotation, U extends TypeLikeUtility<?>>
        implements TCriterionFactory<A, Object> {

    /** Utilities. */
    private final List<U> utilities = initTypeLikeUtilities();

    /**
     * Initialize type-like utilities.
     *
     * @return type-like utilities
     */
    protected abstract List<U> initTypeLikeUtilities();

    @Override
    public final boolean appliesTo(Type type, A constraint) {
        return utilities.stream().anyMatch(u -> u.appliesTo(type));
    }

    /**
     * Require utility for the specified type.
     *
     * @param type type
     * @return utility for the type
     */
    public U requireTypeLikeUtility(Type type) {
        for (U utility : utilities) {
            if (utility.appliesTo(type)) {
                return utility;
            }
        }
        throw new ConstraintAnalysisException("No utility applies to type[%s]", type);
    }

}
