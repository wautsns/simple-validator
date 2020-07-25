package com.github.wautsns.simplevalidator.kernal.criterion.wrapper;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForBoolean;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForByte;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForChar;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForDouble;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForFloat;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForInt;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForLong;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForShort;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Criterion wrapper for array component.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriterionWrapperForArrayComponent extends CriterionWrapper {

    /** {@code CriterionWrapperForArrayComponent} instance. */
    public static final CriterionWrapperForArrayComponent INSTANCE = new CriterionWrapperForArrayComponent();

    @Override
    public <T> CriterionForNonPrimitive<T[]> wrap(CriterionForNonPrimitive<T> criterion) {
        return new CriterionForNonPrimitive<T[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(T[] array) {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            }
        };
    }

    @Override
    public CriterionForNonPrimitive<boolean[]> wrap(CriterionForBoolean criterion) {
        return new CriterionForNonPrimitive<boolean[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(boolean[] array) {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            }
        };
    }

    @Override
    public CriterionForNonPrimitive<char[]> wrap(CriterionForChar criterion) {
        return new CriterionForNonPrimitive<char[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(char[] array) {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            }
        };
    }

    @Override
    public CriterionForNonPrimitive<byte[]> wrap(CriterionForByte criterion) {
        return new CriterionForNonPrimitive<byte[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(byte[] array) {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            }
        };
    }

    @Override
    public CriterionForNonPrimitive<short[]> wrap(CriterionForShort criterion) {
        return new CriterionForNonPrimitive<short[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(short[] array) {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            }
        };
    }

    @Override
    public CriterionForNonPrimitive<int[]> wrap(CriterionForInt criterion) {
        return new CriterionForNonPrimitive<int[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(int[] array) {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            }
        };
    }

    @Override
    public CriterionForNonPrimitive<long[]> wrap(CriterionForLong criterion) {
        return new CriterionForNonPrimitive<long[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(long[] array) {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            }
        };
    }

    @Override
    public CriterionForNonPrimitive<float[]> wrap(CriterionForFloat criterion) {
        return new CriterionForNonPrimitive<float[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(float[] array) {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            }
        };
    }

    @Override
    public CriterionForNonPrimitive<double[]> wrap(CriterionForDouble criterion) {
        return new CriterionForNonPrimitive<double[]>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(double[] array) {
                for (int i = 0; i < array.length; i++) {
                    ValidationFailure failure = criterion.test(array[i]);
                    if (failure != null) { return failure.addIndicator(i); }
                }
                return null;
            }
        };
    }

}
