package com.github.wautsns.simplevalidator.kernal.criterion.wrapper;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForPrimitive;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Criterion wrapper for map key.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriterionWrapperForMapKey extends CriterionWrapper {

    /** {@code CriterionWrapperForMapKey} instance. */
    public static final CriterionWrapperForMapKey INSTANCE = new CriterionWrapperForMapKey();

    @Override
    public <T> CriterionForNonPrimitive<Map<T, ?>> wrap(CriterionForNonPrimitive<T> criterion) {
        return new CriterionForNonPrimitive<Map<T, ?>>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Map<T, ?> map) {
                for (Map.Entry<T, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.test(entry.getKey());
                    if (failure != null) { return failure.addIndicator(entry.getKey()); }
                }
                return null;
            }
        };
    }

    @Override
    public <T> CriterionForNonPrimitive<Map<T, ?>> wrap(CriterionForPrimitive<T> criterion) {
        return new CriterionForNonPrimitive<Map<T, ?>>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(Map<T, ?> map) {
                for (Map.Entry<T, ?> entry : map.entrySet()) {
                    ValidationFailure failure = criterion.testWrappedPrimitiveValue(entry.getKey());
                    if (failure != null) { return failure.addIndicator(entry.getKey()); }
                }
                return null;
            }
        };
    }

}
