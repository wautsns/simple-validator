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
package com.github.wautsns.simplevalidator.model.node;

import com.github.wautsns.simplevalidator.model.criterion.basic.BooleanCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.ByteCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.CharCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.DoubleCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.FloatCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.IntCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.LongCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.ShortCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

/**
 * Constrained getter node.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Getter
public class ConstrainedGetter extends ConstrainedType {

    private final ConstrainedClass declaringClass;
    private final Method origin;
    private final CriterionWrapper criterionWrapper;

    /**
     * Get the declaring class node of the getter.
     *
     * @return the declaring class node of the getter
     * @see #getDeclaringClass()
     */
    @Override
    public ConstrainedNode getParent() {
        return getDeclaringClass();
    }

    public ConstrainedGetter(ConstrainedClass declaringClass, Method getter) {
        super(
                getter.getGenericReturnType(), new Location(declaringClass, getName(getter)),
                getter.getAnnotatedReturnType());
        this.declaringClass = declaringClass;
        this.origin = getter;
        this.criterionWrapper = new CriterionWrapper(getter);
    }

    // -------------------- utils -------------------------------------------------------

    /**
     * Get getter name like '#age()'.
     *
     * @param getter getter
     * @return getter name
     */
    public static String getName(Method getter) {
        return '#' + ReflectionUtils.getPropertyName(getter) + "()";
    }

    @RequiredArgsConstructor
    private static class CriterionWrapper implements Criterion.Wrapper {

        private final @NonNull Method getter;

        @Override
        public <T> TCriterion<?> wrapTCriterion(TCriterion<T> criterion) {
            return source -> criterion.test(ReflectionUtils.invoke(source, getter));
        }

        @Override
        public TCriterion<?> wrapBooleanCriterion(BooleanCriterion criterion) {
            return source -> criterion.testWrappedValue(ReflectionUtils.invoke(source, getter));
        }

        @Override
        public TCriterion<?> wrapCharCriterion(CharCriterion criterion) {
            return source -> criterion.testWrappedValue(ReflectionUtils.invoke(source, getter));
        }

        @Override
        public TCriterion<?> wrapByteCriterion(ByteCriterion criterion) {
            return source -> criterion.testWrappedValue(ReflectionUtils.invoke(source, getter));
        }

        @Override
        public TCriterion<?> wrapShortCriterion(ShortCriterion criterion) {
            return source -> criterion.testWrappedValue(ReflectionUtils.invoke(source, getter));
        }

        @Override
        public TCriterion<?> wrapIntCriterion(IntCriterion criterion) {
            return source -> criterion.testWrappedValue(ReflectionUtils.invoke(source, getter));
        }

        @Override
        public TCriterion<?> wrapLongCriterion(LongCriterion criterion) {
            return source -> criterion.testWrappedValue(ReflectionUtils.invoke(source, getter));
        }

        @Override
        public TCriterion<?> wrapFloatCriterion(FloatCriterion criterion) {
            return source -> criterion.testWrappedValue(ReflectionUtils.invoke(source, getter));
        }

        @Override
        public TCriterion<?> wrapDoubleCriterion(DoubleCriterion criterion) {
            return source -> criterion.testWrappedValue(ReflectionUtils.invoke(source, getter));
        }

    }

}
