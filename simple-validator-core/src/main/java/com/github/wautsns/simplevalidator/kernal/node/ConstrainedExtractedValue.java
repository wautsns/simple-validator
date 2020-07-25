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

import com.github.wautsns.simplevalidator.kernal.constraint.Constraint;
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
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.BooleanValueExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.ByteValueExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.CharValueExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.DoubleValueExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.FloatValueExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.IntValueExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.LongValueExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.NonPrimitiveValueExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.ShortValueExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.value.basic.ValueExtractor;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
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
    private final ValueExtractor valueExtractor;
    /** Criterion wrapper. */
    private final CriterionWrapper criterionWrapper;

    @Override
    public List<? extends ConstrainedNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public CriterionWrapper getCriterionWrapper() {
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
        super(
                new Location(parent, valueExtractor.getNameOfExtractedValue()),
                valueExtractor.getTypeOfExtractedValue(),
                constraints);
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
    @SuppressWarnings("unchecked")
    private static CriterionWrapper generateCriterionWrapper(ValueExtractor valueExtractor) {
        if (valueExtractor instanceof NonPrimitiveValueExtractor) {
            return new CriterionWrapper() {
                @Override
                protected <T> CriterionForNonPrimitive<?> wrap(CriterionForNonPrimitive<T> criterion) {
                    return new CriterionForNonPrimitive<Object>() {
                        @Override
                        protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                            T value = ((NonPrimitiveValueExtractor<Object, T>) valueExtractor).extract(source);
                            return criterion.test(value);
                        }
                    };
                }
            };
        } else if (valueExtractor instanceof BooleanValueExtractor) {
            return new CriterionWrapper() {
                @Override
                protected Criterion wrap(CriterionForBoolean criterion) {
                    return new CriterionForNonPrimitive<Object>() {
                        @Override
                        protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                            boolean value = ((BooleanValueExtractor<Object>) valueExtractor).extract(source);
                            return criterion.test(value);
                        }
                    };
                }
            };
        } else if (valueExtractor instanceof IntValueExtractor) {
            return new CriterionWrapper() {
                @Override
                protected Criterion wrap(CriterionForInt criterion) {
                    return new CriterionForNonPrimitive<Object>() {
                        @Override
                        protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                            int value = ((IntValueExtractor<Object>) valueExtractor).extract(source);
                            return criterion.test(value);
                        }
                    };
                }
            };
        } else if (valueExtractor instanceof LongValueExtractor) {
            return new CriterionWrapper() {
                @Override
                protected Criterion wrap(CriterionForLong criterion) {
                    return new CriterionForNonPrimitive<Object>() {
                        @Override
                        protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                            long value = ((LongValueExtractor<Object>) valueExtractor).extract(source);
                            return criterion.test(value);
                        }
                    };
                }
            };
        } else if (valueExtractor instanceof ByteValueExtractor) {
            return new CriterionWrapper() {
                @Override
                protected Criterion wrap(CriterionForByte criterion) {
                    return new CriterionForNonPrimitive<Object>() {
                        @Override
                        protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                            byte value = ((ByteValueExtractor<Object>) valueExtractor).extract(source);
                            return criterion.test(value);
                        }
                    };
                }
            };
        } else if (valueExtractor instanceof DoubleValueExtractor) {
            return new CriterionWrapper() {
                @Override
                protected Criterion wrap(CriterionForDouble criterion) {
                    return new CriterionForNonPrimitive<Object>() {
                        @Override
                        protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                            double value = ((DoubleValueExtractor<Object>) valueExtractor).extract(source);
                            return criterion.test(value);
                        }
                    };
                }
            };
        } else if (valueExtractor instanceof CharValueExtractor) {
            return new CriterionWrapper() {
                @Override
                protected Criterion wrap(CriterionForChar criterion) {
                    return new CriterionForNonPrimitive<Object>() {
                        @Override
                        protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                            char value = ((CharValueExtractor<Object>) valueExtractor).extract(source);
                            return criterion.test(value);
                        }
                    };
                }
            };
        } else if (valueExtractor instanceof ShortValueExtractor) {
            return new CriterionWrapper() {
                @Override
                protected Criterion wrap(CriterionForShort criterion) {
                    return new CriterionForNonPrimitive<Object>() {
                        @Override
                        protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                            short value = ((ShortValueExtractor<Object>) valueExtractor).extract(source);
                            return criterion.test(value);
                        }
                    };
                }
            };
        } else if (valueExtractor instanceof FloatValueExtractor) {
            return new CriterionWrapper() {
                @Override
                protected Criterion wrap(CriterionForFloat criterion) {
                    return new CriterionForNonPrimitive<Object>() {
                        @Override
                        protected ValidationFailure testWithoutEnhancingFailure(Object source) {
                            float value = ((FloatValueExtractor<Object>) valueExtractor).extract(source);
                            return criterion.test(value);
                        }
                    };
                }
            };
        } else {
            throw new IllegalStateException();
        }

    }

}
