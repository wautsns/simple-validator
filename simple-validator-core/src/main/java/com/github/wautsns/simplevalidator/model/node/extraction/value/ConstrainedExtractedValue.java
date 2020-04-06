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
package com.github.wautsns.simplevalidator.model.node.extraction.value;

import com.github.wautsns.simplevalidator.model.constraint.Constraint;
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
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.extractor.BooleanExtractor;
import com.github.wautsns.simplevalidator.util.extractor.ByteExtractor;
import com.github.wautsns.simplevalidator.util.extractor.CharExtractor;
import com.github.wautsns.simplevalidator.util.extractor.DoubleExtractor;
import com.github.wautsns.simplevalidator.util.extractor.FloatExtractor;
import com.github.wautsns.simplevalidator.util.extractor.IntExtractor;
import com.github.wautsns.simplevalidator.util.extractor.LongExtractor;
import com.github.wautsns.simplevalidator.util.extractor.ShortExtractor;
import com.github.wautsns.simplevalidator.util.extractor.TExtractor;
import com.github.wautsns.simplevalidator.util.extractor.ValueExtractor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * The constrained extracted value.
 *
 * @author wautsns
 * @since Mar 21, 2020
 */
@Getter
public class ConstrainedExtractedValue extends ConstrainedNode {

    /** parent */
    private final ConstrainedNode parent;
    /** value extractor */
    private final ValueExtractor valueExtractor;
    /** criterion wrapper */
    private final Criterion.Wrapper criterionWrapper;

    @Override
    public List<? extends ConstrainedNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return criterionWrapper;
    }

    // #################### constructor #################################################

    /**
     * Construct a constrained extracted value.
     *
     * @param parent parent
     * @param valueExtractor value extractor
     * @param constraints constraints
     */
    public ConstrainedExtractedValue(
            ConstrainedNode parent, ValueExtractor valueExtractor, List<Constraint<?>> constraints) {
        super(new Location(parent, valueExtractor.getName()), valueExtractor.getExtractedValueType(), constraints);
        this.parent = parent;
        this.valueExtractor = valueExtractor;
        this.criterionWrapper = generateCriterionWrapper(valueExtractor);
    }

    // #################### metadata ####################################################

    /**
     * Generate criterion wrapper.
     *
     * @param valueExtractor value extractor
     * @return criterion wrapper.
     */
    private static Criterion.Wrapper generateCriterionWrapper(ValueExtractor valueExtractor) {
        if (valueExtractor instanceof TCriterion) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected <T> TCriterion<?> wrap(TCriterion<T> criterion) {
                    return target -> criterion.test(((TExtractor<Object, T>) valueExtractor).extract(target));
                }

            };
        } else if (valueExtractor instanceof BooleanExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(BooleanCriterion criterion) {
                    return target -> criterion.test(((BooleanExtractor<Object>) valueExtractor).extract(target));
                }

            };
        } else if (valueExtractor instanceof IntExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(IntCriterion criterion) {
                    return target -> criterion.test(((IntExtractor<Object>) valueExtractor).extract(target));
                }

            };
        } else if (valueExtractor instanceof LongExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(LongCriterion criterion) {
                    return target -> criterion.test(((LongExtractor<Object>) valueExtractor).extract(target));
                }

            };
        } else if (valueExtractor instanceof ByteExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(ByteCriterion criterion) {
                    return target -> criterion.test(((ByteExtractor<Object>) valueExtractor).extract(target));
                }

            };
        } else if (valueExtractor instanceof DoubleExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(DoubleCriterion criterion) {
                    return target -> criterion.test(((DoubleExtractor<Object>) valueExtractor).extract(target));
                }

            };
        } else if (valueExtractor instanceof CharExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(CharCriterion criterion) {
                    return target -> criterion.test(((CharExtractor<Object>) valueExtractor).extract(target));
                }

            };
        } else if (valueExtractor instanceof ShortExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(ShortCriterion criterion) {
                    return target -> criterion.test(((ShortExtractor<Object>) valueExtractor).extract(target));
                }

            };
        } else if (valueExtractor instanceof FloatExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(FloatCriterion criterion) {
                    return target -> criterion.test(((FloatExtractor<Object>) valueExtractor).extract(target));
                }

            };
        } else {
            throw new IllegalStateException();
        }

    }

}
