package com.github.wautsns.simplevalidator.constraint.business.id;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.constraint.number.pository.VPositive;
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;
import com.github.wautsns.templatemessage.variable.Variable;

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
@VPositive
public @interface VId {

    List<CriterionFactory<VId, ?, ?>> CRITERION_FACTORY_LIST = new LinkedList<>(Collections.singletonList(
            new VIdIntegerLongBigIntegerCriterionFactory()
    ));

    String message() default "[`VId`]";

    int order() default 0;

    boolean unsigned() default false;

    // ------------------------- variables -----------------------------------------

    Variable<Boolean> UNSIGNED = new Variable<>("unsigned");

}
