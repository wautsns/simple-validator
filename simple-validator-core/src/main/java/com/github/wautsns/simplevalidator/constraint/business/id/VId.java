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

    /**
     * Message(template).
     *
     * @return message(template)
     */
    String message() default "[`VId`]";

    /**
     * Order of the constraint.
     *
     * @return order of the constraint
     */
    int order() default 0;

    boolean unsigned() default false;

    // #################### extra #######################################################

    /** built-in criterion factories */
    List<CriterionFactory<VId, ?, ?>> CRITERION_FACTORIES = new LinkedList<>(Collections.singletonList(
            VIdIntegerLongBigIntegerCriterionFactory.INSTANCE
    ));

    // ==================== variables ===================================================

    /** variables: {@linkplain #unsigned() unsigned} */
    Variable<Boolean> UNSIGNED = new Variable<>("unsigned");

}
