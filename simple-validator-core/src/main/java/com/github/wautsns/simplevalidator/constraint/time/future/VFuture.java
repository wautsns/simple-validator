package com.github.wautsns.simplevalidator.constraint.time.future;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.TypeLikeUtilityVariableCache;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.TimeLikeUtility;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
@AConstraint(criterionFactories = {
        VFutureTimeLikeCriterionFactory.class
})
public @interface VFuture {

    String message() default "[`VFuture`]";

    int order() default 0;

    long years() default 0;

    long months() default 0;

    long days() default 0;

    long hours() default 0;

    long minutes() default 0;

    long seconds() default 0;

    long milliseconds() default 0;

    TypeLikeUtilityVariableCache<TimeLikeUtility<?>> REF = new TypeLikeUtilityVariableCache<>("ref");

}
