package com.github.wautsns.simplevalidator.kernal.criterion.wrapper;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForBoolean;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForByte;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForChar;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForDouble;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForFloat;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForInt;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForLong;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForShort;

/**
 * Criterion wrapper.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
public abstract class CriterionWrapper {

    /**
     * Wrap the criterion.
     *
     * @param criterion criterion
     * @return criterion
     */
    public final Criterion wrap(Criterion criterion) {
        if (criterion instanceof CriterionForNonPrimitive) { return wrap((CriterionForNonPrimitive<?>) criterion); }
        if (!(criterion instanceof CriterionForPrimitive)) { throw new IllegalStateException(); }
        CriterionForNonPrimitive<?> wrappedCriterion = wrap((CriterionForPrimitive<?>) criterion);
        if (wrappedCriterion != null) { return wrappedCriterion; }
        if (criterion instanceof CriterionForInt) { return wrap((CriterionForInt) criterion); }
        if (criterion instanceof CriterionForBoolean) { return wrap((CriterionForBoolean) criterion); }
        if (criterion instanceof CriterionForLong) { return wrap((CriterionForLong) criterion); }
        if (criterion instanceof CriterionForByte) { return wrap((CriterionForByte) criterion); }
        if (criterion instanceof CriterionForChar) { return wrap((CriterionForChar) criterion); }
        if (criterion instanceof CriterionForDouble) { return wrap((CriterionForDouble) criterion); }
        if (criterion instanceof CriterionForFloat) { return wrap((CriterionForFloat) criterion); }
        if (criterion instanceof CriterionForShort) { return wrap((CriterionForShort) criterion); }
        throw new IllegalStateException();
    }

    /**
     * Wrap the criterion.
     *
     * @param criterion criterion
     * @param <T> type of value to be validated of criterion
     * @return TCriterion
     */
    protected <T> CriterionForNonPrimitive<?> wrap(CriterionForNonPrimitive<T> criterion) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wrap the criterion.
     *
     * @param criterion primitive criterion
     * @param <T> type of wrapped value
     * @return TCriterion, or {@code null} if not supported
     */
    protected <T> CriterionForNonPrimitive<?> wrap(CriterionForPrimitive<T> criterion) {
        return null;
    }

    /**
     * Wrap the criterion.
     *
     * @param criterion criterion
     * @return criterion
     */
    protected Criterion wrap(CriterionForBoolean criterion) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wrap the criterion.
     *
     * @param criterion criterion
     * @return criterion
     */
    protected Criterion wrap(CriterionForChar criterion) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wrap the criterion.
     *
     * @param criterion criterion
     * @return criterion
     */
    protected Criterion wrap(CriterionForByte criterion) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wrap the criterion.
     *
     * @param criterion criterion
     * @return criterion
     */
    protected Criterion wrap(CriterionForShort criterion) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wrap the criterion.
     *
     * @param criterion criterion
     * @return criterion
     */
    protected Criterion wrap(CriterionForInt criterion) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wrap the criterion.
     *
     * @param criterion criterion
     * @return criterion
     */
    protected Criterion wrap(CriterionForLong criterion) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wrap the criterion.
     *
     * @param criterion criterion
     * @return criterion
     */
    protected Criterion wrap(CriterionForFloat criterion) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wrap the criterion.
     *
     * @param criterion criterion
     * @return criterion
     */
    protected Criterion wrap(CriterionForDouble criterion) {
        throw new UnsupportedOperationException();
    }

}
