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
package com.github.wautsns.simplevalidator.kernal.node;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForBoolean;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForByte;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForChar;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForDouble;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForFloat;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForInt;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForLong;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForShort;
import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapper;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.util.common.ReflectionUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;

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
        this.criterionWrapper = new CriterionWrapperForField(field);
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
    private static class CriterionWrapperForField extends CriterionWrapper {

        /** Accessible field. */
        private final Field field;

        @Override
        protected <T> CriterionForNonPrimitive<?> wrap(CriterionForNonPrimitive<T> criterion) {
            return new CriterionForNonPrimitive<Object>() {
                @Override
                protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                    return criterion.test(ReflectionUtils.getValue(source, field));
                }
            };
        }

        @Override
        protected Criterion wrap(CriterionForBoolean criterion) {
            return new CriterionForNonPrimitive<Object>() {
                @Override
                protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                    return criterion.test(ReflectionUtils.getBoolean(source, field));
                }
            };
        }

        @Override
        protected Criterion wrap(CriterionForChar criterion) {
            return new CriterionForNonPrimitive<Object>() {
                @Override
                protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                    return criterion.test(ReflectionUtils.getChar(source, field));
                }
            };
        }

        @Override
        protected Criterion wrap(CriterionForByte criterion) {
            return new CriterionForNonPrimitive<Object>() {
                @Override
                protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                    return criterion.test(ReflectionUtils.getByte(source, field));
                }
            };
        }

        @Override
        protected Criterion wrap(CriterionForShort criterion) {
            return new CriterionForNonPrimitive<Object>() {
                @Override
                protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                    return criterion.test(ReflectionUtils.getShort(source, field));
                }
            };
        }

        @Override
        protected Criterion wrap(CriterionForInt criterion) {
            return new CriterionForNonPrimitive<Object>() {
                @Override
                protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                    return criterion.test(ReflectionUtils.getInt(source, field));
                }
            };
        }

        @Override
        protected Criterion wrap(CriterionForLong criterion) {
            return new CriterionForNonPrimitive<Object>() {
                @Override
                protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                    return criterion.test(ReflectionUtils.getLong(source, field));
                }
            };
        }

        @Override
        protected Criterion wrap(CriterionForFloat criterion) {
            return new CriterionForNonPrimitive<Object>() {
                @Override
                protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                    return criterion.test(ReflectionUtils.getFloat(source, field));
                }
            };
        }

        @Override
        protected Criterion wrap(CriterionForDouble criterion) {
            return new CriterionForNonPrimitive<Object>() {
                @Override
                protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                    return criterion.test(ReflectionUtils.getDouble(source, field));
                }
            };
        }

    }

}
