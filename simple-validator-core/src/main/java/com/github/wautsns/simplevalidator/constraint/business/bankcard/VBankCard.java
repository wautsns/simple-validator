package com.github.wautsns.simplevalidator.constraint.business.bankcard;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.constraint.algorithm.luhn.VLuhn;

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
@Target({ ANNOTATION_TYPE, FIELD, METHOD, PARAMETER, TYPE_USE })
@AConstraint
@VLuhn
public @interface VBankCard {

    /**
     * Message(template).
     *
     * @return message(template)
     */
    String message() default "[`VBankCard`]";

    /**
     * Order of the constraint.
     *
     * @return order of the constraint
     */
    int order() default 0;

}
