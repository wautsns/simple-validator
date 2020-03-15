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

import java.lang.reflect.Field;

/**
 * Constrained field node.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Getter
public class ConstrainedField extends ConstrainedType {

    private final ConstrainedClass declaringClass;
    private final Field origin;
    private final CriterionWrapper criterionWrapper;

    /**
     * Get the declaring class node of the field.
     *
     * @return the declaring class node of the field
     * @see #getDeclaringClass()
     */
    @Override
    public ConstrainedNode getParent() {
        return getDeclaringClass();
    }

    public ConstrainedField(ConstrainedClass declaringClass, Field field) {
        super(field.getGenericType(), new Location(declaringClass, getName(field)), field.getAnnotatedType());
        this.declaringClass = declaringClass;
        this.origin = field;
        this.criterionWrapper = new CriterionWrapper(field);
    }

    // -------------------- utils -------------------------------------------------------

    /**
     * Get field name like '#age'.
     *
     * @param field field
     * @return field name
     */
    public static String getName(Field field) {
        return '#' + field.getName();
    }

    @RequiredArgsConstructor
    private static class CriterionWrapper implements Criterion.Wrapper {

        private final @NonNull Field field;

        @Override
        public <T> TCriterion<?> wrapTCriterion(TCriterion<T> criterion) {
            return source -> criterion.test(ReflectionUtils.getValue(source, field));
        }

        @Override
        public TCriterion<?> wrapBooleanCriterion(BooleanCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getBoolean(source, field));
        }

        @Override
        public TCriterion<?> wrapCharCriterion(CharCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getChar(source, field));
        }

        @Override
        public TCriterion<?> wrapByteCriterion(ByteCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getByte(source, field));
        }

        @Override
        public TCriterion<?> wrapShortCriterion(ShortCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getShort(source, field));
        }

        @Override
        public TCriterion<?> wrapIntCriterion(IntCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getInt(source, field));
        }

        @Override
        public TCriterion<?> wrapLongCriterion(LongCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getLong(source, field));
        }

        @Override
        public TCriterion<?> wrapFloatCriterion(FloatCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getFloat(source, field));
        }

        @Override
        public TCriterion<?> wrapDoubleCriterion(DoubleCriterion criterion) {
            return source -> criterion.test(ReflectionUtils.getDouble(source, field));
        }

    }

}
