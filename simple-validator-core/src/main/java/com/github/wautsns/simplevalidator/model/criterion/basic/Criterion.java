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

    /** criterion wrapper */
    interface Wrapper {

        /**
         * Wrap the criterion to new criterion.
         *
         * @param criterion criterion
         * @param <T> type of value to be validated of criterion
         * @return new criterion
         */
        <T> Criterion wrapTCriterion(TCriterion<T> criterion);

        /**
         * Wrap the criterion to new criterion.
         *
         * @param criterion criterion
         * @return new criterion
         */
        Criterion wrapBooleanCriterion(BooleanCriterion criterion);

        /**
         * Wrap the criterion to new criterion.
         *
         * @param criterion criterion
         * @return new criterion
         */
        Criterion wrapCharCriterion(CharCriterion criterion);

        /**
         * Wrap the criterion to new criterion.
         *
         * @param criterion criterion
         * @return new criterion
         */
        Criterion wrapByteCriterion(ByteCriterion criterion);

        /**
         * Wrap the criterion to new criterion.
         *
         * @param criterion criterion
         * @return new criterion
         */
        Criterion wrapShortCriterion(ShortCriterion criterion);

        /**
         * Wrap the criterion to new criterion.
         *
         * @param criterion criterion
         * @return new criterion
         */
        Criterion wrapIntCriterion(IntCriterion criterion);

        /**
         * Wrap the criterion to new criterion.
         *
         * @param criterion criterion
         * @return new criterion
         */
        Criterion wrapLongCriterion(LongCriterion criterion);

        /**
         * Wrap the criterion to new criterion.
         *
         * @param criterion criterion
         * @return new criterion
         */
        Criterion wrapFloatCriterion(FloatCriterion criterion);

        /**
         * Wrap the criterion to new criterion.
         *
         * @param criterion criterion
         * @return new criterion
         */
        Criterion wrapDoubleCriterion(DoubleCriterion criterion);

        /**
         * Wrap the criterion to new criterion.
         *
         * @param criterion criterion
         * @return new criterion
         */
        default Criterion wrap(Criterion criterion) {
            if (criterion instanceof TCriterion) {
                return wrapTCriterion((TCriterion<?>) criterion);
            } else if (criterion instanceof BooleanCriterion) {
                return wrapBooleanCriterion((BooleanCriterion) criterion);
            } else if (criterion instanceof CharCriterion) {
                return wrapCharCriterion((CharCriterion) criterion);
            } else if (criterion instanceof ByteCriterion) {
                return wrapByteCriterion((ByteCriterion) criterion);
            } else if (criterion instanceof ShortCriterion) {
                return wrapShortCriterion((ShortCriterion) criterion);
            } else if (criterion instanceof IntCriterion) {
                return wrapIntCriterion((IntCriterion) criterion);
            } else if (criterion instanceof LongCriterion) {
                return wrapLongCriterion((LongCriterion) criterion);
            } else if (criterion instanceof FloatCriterion) {
                return wrapFloatCriterion((FloatCriterion) criterion);
            } else if (criterion instanceof DoubleCriterion) {
                return wrapDoubleCriterion((DoubleCriterion) criterion);
            } else {
                throw new IllegalStateException();
            }
        }

    }

}
