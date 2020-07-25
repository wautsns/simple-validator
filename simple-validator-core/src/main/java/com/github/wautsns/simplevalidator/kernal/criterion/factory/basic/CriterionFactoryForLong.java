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
package com.github.wautsns.simplevalidator.kernal.criterion.factory.basic;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForLong;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForLong;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Criterion factory for {@code long} type.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class CriterionFactoryForLong<A extends Annotation>
        extends CriterionFactory<A, CriteriaForLong, CriterionForLong> {

    @Override
    public final boolean applyTo(Type type, A constraint) {
        return (long.class == type);
    }

}
