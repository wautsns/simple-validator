package com.github.wautsns.simplevalidator.constraint.business.bankcard;

import com.github.wautsns.simplevalidator.constraint.AConstraint;

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
        VBankCardTypeExtendsCharSequenceCriterionFactory.class
})
public @interface VBankCard {

    String message() default "[`VBankCard`]";

    int order() default 0;

}
