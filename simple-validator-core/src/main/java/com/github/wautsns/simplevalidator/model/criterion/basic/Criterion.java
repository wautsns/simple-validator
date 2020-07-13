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
package com.github.wautsns.simplevalidator.model.criterion.basic;

import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;

import java.util.function.UnaryOperator;

/**
 * Criterion.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public interface Criterion {

    /**
     * Enhance failure(only if the failure is not {@code null}).
     *
     * @param enhancer failure enhancer
     * @return new criterion
     */
    Criterion enhanceFailure(UnaryOperator<ValidationFailure> enhancer);

    /** Criterion wrapper. */
    abstract class Wrapper {

        /**
         * Wrap the criterion.
         *
         * @param criterion criterion
         * @return criterion
         */
        public final Criterion wrap(Criterion criterion) {
            if (criterion instanceof TCriterion) { return wrap((TCriterion<?>) criterion); }
            if (!(criterion instanceof PrimitiveCriterion)) { throw new IllegalStateException(); }
            TCriterion<?> wrappedCriterion = wrap((PrimitiveCriterion<?>) criterion);
            if (wrappedCriterion != null) { return wrappedCriterion; }
            if (criterion instanceof IntCriterion) { return wrap((IntCriterion) criterion); }
            if (criterion instanceof BooleanCriterion) { return wrap((BooleanCriterion) criterion); }
            if (criterion instanceof LongCriterion) { return wrap((LongCriterion) criterion); }
            if (criterion instanceof ByteCriterion) { return wrap((ByteCriterion) criterion); }
            if (criterion instanceof CharCriterion) { return wrap((CharCriterion) criterion); }
            if (criterion instanceof DoubleCriterion) { return wrap((DoubleCriterion) criterion); }
            if (criterion instanceof FloatCriterion) { return wrap((FloatCriterion) criterion); }
            if (criterion instanceof ShortCriterion) { return wrap((ShortCriterion) criterion); }
            throw new IllegalStateException();
        }

        /**
         * Wrap the criterion.
         *
         * @param criterion criterion
         * @param <T> type of value to be validated of criterion
         * @return TCriterion
         */
        protected <T> TCriterion<?> wrap(TCriterion<T> criterion) {
            throw new UnsupportedOperationException();
        }

        /**
         * Wrap the criterion.
         *
         * @param criterion primitive criterion
         * @param <W> type of wrapped value
         * @return TCriterion, or {@code null} if not supported
         */
        protected <W> TCriterion<?> wrap(PrimitiveCriterion<W> criterion) {
            return null;
        }

        /**
         * Wrap the criterion.
         *
         * @param criterion criterion
         * @return criterion
         */
        protected Criterion wrap(BooleanCriterion criterion) {
            throw new UnsupportedOperationException();
        }

        /**
         * Wrap the criterion.
         *
         * @param criterion criterion
         * @return criterion
         */
        protected Criterion wrap(CharCriterion criterion) {
            throw new UnsupportedOperationException();
        }

        /**
         * Wrap the criterion.
         *
         * @param criterion criterion
         * @return criterion
         */
        protected Criterion wrap(ByteCriterion criterion) {
            throw new UnsupportedOperationException();
        }

        /**
         * Wrap the criterion.
         *
         * @param criterion criterion
         * @return criterion
         */
        protected Criterion wrap(ShortCriterion criterion) {
            throw new UnsupportedOperationException();
        }

        /**
         * Wrap the criterion.
         *
         * @param criterion criterion
         * @return criterion
         */
        protected Criterion wrap(IntCriterion criterion) {
            throw new UnsupportedOperationException();
        }

        /**
         * Wrap the criterion.
         *
         * @param criterion criterion
         * @return criterion
         */
        protected Criterion wrap(LongCriterion criterion) {
            throw new UnsupportedOperationException();
        }

        /**
         * Wrap the criterion.
         *
         * @param criterion criterion
         * @return criterion
         */
        protected Criterion wrap(FloatCriterion criterion) {
            throw new UnsupportedOperationException();
        }

        /**
         * Wrap the criterion.
         *
         * @param criterion criterion
         * @return criterion
         */
        protected Criterion wrap(DoubleCriterion criterion) {
            throw new UnsupportedOperationException();
        }

    }

}
