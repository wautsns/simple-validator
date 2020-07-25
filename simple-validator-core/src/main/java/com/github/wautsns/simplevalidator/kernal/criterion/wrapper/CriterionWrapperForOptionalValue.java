package com.github.wautsns.simplevalidator.kernal.criterion.wrapper;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForPrimitive;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Criterion wrapper for optional value.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriterionWrapperForOptionalValue extends CriterionWrapper {

    /** {@code CriterionWrapperForOptionalValue} instance. */
    public static final CriterionWrapperForOptionalValue INSTANCE = new CriterionWrapperForOptionalValue();

    @Override
    public <T> CriterionForNonPrimitive<Optional<T>> wrap(CriterionForNonPrimitive<T> criterion) {
        return new CriterionForNonPrimitive<Optional<T>>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Optional<T> optionalValue) {
                return criterion.test(optionalValue.orElse(null));
            }
        };
    }

    @Override
    public <T> CriterionForNonPrimitive<Optional<T>> wrap(CriterionForPrimitive<T> criterion) {
        return new CriterionForNonPrimitive<Optional<T>>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Optional<T> optionalValue) {
                return criterion.testWrappedPrimitiveValue(optionalValue.orElseThrow(NullPointerException::new));
            }
        };
    }

}
