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

import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.PrimitiveCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

/**
 * The constrained getter.
 *
 * @author wautsns
 * @since Mar 19, 2020
 */
@Getter
public class ConstrainedGetter extends ConstrainedTypeContainer {

    /** Declaring class. */
    private final ConstrainedClass declaringClass;
    /** Original getter. */
    private final Method origin;
    /** Criterion wrapper. */
    private final CriterionWrapper criterionWrapper;

    /**
     * Get declaring class.
     *
     * @return declaring class
     */
    @Override
    public ConstrainedNode getParent() {
        return getDeclaringClass();
    }

    // #################### constructor #################################################

    /**
     * Construct a constrained getter.
     *
     * @param declaringClass declaring class
     * @param getter getter
     */
    ConstrainedGetter(ConstrainedClass declaringClass, Method getter) {
        super(declaringClass, generateName(getter), getter.getAnnotatedReturnType());
        this.declaringClass = declaringClass;
        getter.setAccessible(true);
        this.origin = getter;
        this.criterionWrapper = new CriterionWrapper(getter);
    }

    // #################### utils #######################################################

    /**
     * Generate getter name like '#age()'.
     *
     * @param getter getter
     * @return getter name
     */
    public static String generateName(Method getter) {
        return '#' + ReflectionUtils.getPropertyName(getter) + "()";
    }

    // ==================== internal utils ==============================================

    /** Criterion wrapper. */
    @RequiredArgsConstructor
    private static class CriterionWrapper extends Criterion.Wrapper {

        /** Accessible getter. */
        private final Method getter;

        @Override
        public <T> TCriterion<?> wrap(TCriterion<T> criterion) {
            return source -> criterion.test(ReflectionUtils.invoke(source, getter));
        }

        @Override
        protected <W> TCriterion<?> wrap(PrimitiveCriterion<W> criterion) {
            return source -> criterion.testWrappedValue(ReflectionUtils.invoke(source, getter));
        }

    }

}
