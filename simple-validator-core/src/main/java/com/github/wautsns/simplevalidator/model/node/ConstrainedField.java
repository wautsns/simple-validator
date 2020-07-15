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
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * The constrained field.
 *
 * @author wautsns
 * @since Mar 19, 2020
 */
@Getter
public class ConstrainedField extends ConstrainedTypeContainer {

    /** Declaring class. */
    private final ConstrainedClass declaringClass;
    /** Original field. */
    private final Field origin;
    /** Criterion wrapper. */
    private final Criterion.Wrapper criterionWrapper;

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
     * Construct a constrained field.
     *
     * @param declaringClass declaring class
     * @param field field
     */
    ConstrainedField(ConstrainedClass declaringClass, Field field) {
        super(declaringClass, generateName(field), field.getAnnotatedType());
        this.declaringClass = declaringClass;
        field.setAccessible(true);
        this.origin = field;
        this.criterionWrapper = new CriterionWrapper(field);
    }

    // #################### utils #######################################################

    /**
     * Generate field name like '#age'.
     *
     * @param field field
     * @return field name
     */
    public static String generateName(Field field) {
        return '#' + field.getName();
    }

    // ==================== internal utils ==============================================

    /** Criterion wrapper. */
    @RequiredArgsConstructor
    private static class CriterionWrapper extends Criterion.Wrapper {

        /** Accessible field. */
        private final Field field;

        @Override
        public <T> TCriterion<?> wrap(TCriterion<T> criterion) {
            return source -> criterion.test(ReflectionUtils.getValue(source, field));
        }

        @Override
        public TCriterion<?> wrap(BooleanCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getBoolean(source, field));
        }

        @Override
        public TCriterion<?> wrap(CharCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getChar(source, field));
        }

        @Override
        public TCriterion<?> wrap(ByteCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getByte(source, field));
        }

        @Override
        public TCriterion<?> wrap(ShortCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getShort(source, field));
        }

        @Override
        public TCriterion<?> wrap(IntCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getInt(source, field));
        }

        @Override
        public TCriterion<?> wrap(LongCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getLong(source, field));
        }

        @Override
        public TCriterion<?> wrap(FloatCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getFloat(source, field));
        }

        @Override
        public TCriterion<?> wrap(DoubleCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getDouble(source, field));
        }

    }

}
