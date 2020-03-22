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
package com.github.wautsns.simplevalidator.model.criterion.factory.primitive;

import com.github.wautsns.simplevalidator.model.criterion.basic.CharCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.CharCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Abstract char criterion factory.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class CharCriterionFactory<A extends Annotation>
        implements CriterionFactory<A, CharCriteria, CharCriterion> {

    @Override
    public final boolean appliesTo(Type type, A constraint) {
        return (char.class == type);
    }

}