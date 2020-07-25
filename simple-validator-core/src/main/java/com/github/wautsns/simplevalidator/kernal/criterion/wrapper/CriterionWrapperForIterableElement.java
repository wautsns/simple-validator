package com.github.wautsns.simplevalidator.kernal.criterion.wrapper;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForPrimitive;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Criterion wrapper for iterable element.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriterionWrapperForIterableElement extends CriterionWrapper {

    /** {@code CriterionWrapperForIterableValueElement} instance. */
    public static final CriterionWrapperForIterableElement INSTANCE = new CriterionWrapperForIterableElement();

    @Override
    public <T> CriterionForNonPrimitive<Iterable<T>> wrap(CriterionForNonPrimitive<T> criterion) {
        return new CriterionForNonPrimitive<Iterable<T>>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Iterable<T> iterableValue) {
                int index = 0;
                for (T element : iterableValue) {
                    ValidationFailure failure = criterion.test(element);
                    if (failure != null) { return failure.addIndicator(index); }
                }
                return null;
            }
        };
    }

    @Override
    public <T> CriterionForNonPrimitive<Iterable<T>> wrap(CriterionForPrimitive<T> criterion) {
        return new CriterionForNonPrimitive<Iterable<T>>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Iterable<T> iterableValue) {
                int index = 0;
                for (T element : iterableValue) {
                    ValidationFailure failure = criterion.testWrappedPrimitiveValue(element);
                    if (failure != null) { return failure.addIndicator(index); }
                }
                return null;
            }
        };
    }

}
