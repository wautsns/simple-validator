package com.github.wautsns.simplevalidator.constraint.business.bankcard;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;

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
public @interface VBankCard {

    List<CriterionFactory<VBankCard, ?, ?>> CRITERION_FACTORIES = new LinkedList<>(Collections.singletonList(
            new VBankCardTypeExtendsCharSequenceCriterionFactory()
    ));

    String message() default "[`VBankCard`]";

    int order() default 0;

}
