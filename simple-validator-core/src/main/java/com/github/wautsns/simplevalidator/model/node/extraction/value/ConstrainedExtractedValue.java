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
import com.github.wautsns.simplevalidator.util.extractor.Extractor;
import com.github.wautsns.simplevalidator.util.extractor.FloatExtractor;
import com.github.wautsns.simplevalidator.util.extractor.IntExtractor;
import com.github.wautsns.simplevalidator.util.extractor.LongExtractor;
import com.github.wautsns.simplevalidator.util.extractor.ShortExtractor;
import com.github.wautsns.simplevalidator.util.extractor.TExtractor;
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

    /** Parent. */
    private final ConstrainedNode parent;
    /** Value extractor. */
    private final Extractor extractor;
    /** Criterion wrapper. */
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
     * @param extractor value extractor
     * @param constraints constraints
     */
    public ConstrainedExtractedValue(
            ConstrainedNode parent, Extractor extractor, List<Constraint<?>> constraints) {
        super(new Location(parent, extractor.getName()), extractor.getExtractedValueType(), constraints);
        this.parent = parent;
        this.extractor = extractor;
        this.criterionWrapper = generateCriterionWrapper(extractor);
    }

    // #################### metadata ####################################################

    /**
     * Generate criterion wrapper.
     *
     * @param extractor value extractor
     * @return criterion wrapper.
     */
    private static Criterion.Wrapper generateCriterionWrapper(Extractor extractor) {
        if (extractor instanceof TExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected <T> TCriterion<?> wrap(TCriterion<T> criterion) {
                    return target -> criterion.test(((TExtractor<Object, T>) extractor).extract(target));
                }

            };
        } else if (extractor instanceof BooleanExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(BooleanCriterion criterion) {
                    return target -> criterion.test(((BooleanExtractor<Object>) extractor).extract(target));
                }

            };
        } else if (extractor instanceof IntExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(IntCriterion criterion) {
                    return target -> criterion.test(((IntExtractor<Object>) extractor).extract(target));
                }

            };
        } else if (extractor instanceof LongExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(LongCriterion criterion) {
                    return target -> criterion.test(((LongExtractor<Object>) extractor).extract(target));
                }

            };
        } else if (extractor instanceof ByteExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(ByteCriterion criterion) {
                    return target -> criterion.test(((ByteExtractor<Object>) extractor).extract(target));
                }

            };
        } else if (extractor instanceof DoubleExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(DoubleCriterion criterion) {
                    return target -> criterion.test(((DoubleExtractor<Object>) extractor).extract(target));
                }

            };
        } else if (extractor instanceof CharExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(CharCriterion criterion) {
                    return target -> criterion.test(((CharExtractor<Object>) extractor).extract(target));
                }

            };
        } else if (extractor instanceof ShortExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(ShortCriterion criterion) {
                    return target -> criterion.test(((ShortExtractor<Object>) extractor).extract(target));
                }

            };
        } else if (extractor instanceof FloatExtractor) {
            return new Criterion.Wrapper() {

                @Override
                @SuppressWarnings("unchecked")
                protected TCriterion<?> wrap(FloatCriterion criterion) {
                    return target -> criterion.test(((FloatExtractor<Object>) extractor).extract(target));
                }

            };
        } else {
            throw new IllegalStateException();
        }

    }

}
