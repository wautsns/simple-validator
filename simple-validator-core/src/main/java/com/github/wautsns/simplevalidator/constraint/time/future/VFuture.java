package com.github.wautsns.simplevalidator.constraint.time.future;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.TypeLikeUtilityVariableCache;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.TimeLikeUtility;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@Documented
@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER, TYPE_USE})
@AConstraint
public @interface VFuture {

    /**
     * Message(template).
     *
     * @return message(template)
     */
    String message() default "[`VFuture`]";

    /**
     * Order of the constraint.
     *
     * @return order of the constraint
     */
    int order() default 0;

    long years() default 0;

    long months() default 0;

    long weeks() default 0;

    long days() default 0;

    long hours() default 0;

    long minutes() default 0;

    long seconds() default 0;

    long milliseconds() default 0;

    // #################### extra #######################################################

    /** built-in criterion factories */
    List<CriterionFactory<VFuture, ?, ?>> CRITERION_FACTORIES = new LinkedList<>(Collections.singletonList(
            VFutureTimeLikeCriterionFactory.INSTANCE
    ));

    // ==================== variables ===================================================

    /** variables: ref */
    TypeLikeUtilityVariableCache<TimeLikeUtility<?>> REF = new TypeLikeUtilityVariableCache<>("ref");

}
