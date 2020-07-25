package com.github.wautsns.simplevalidator.constraint.algorithm.luhn;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.kernal.constraint.ConstraintMetadata;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactory;

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
 * VLuhn.
 *
 * @author wautsns
 * @since Mar 19, 2020
 */
@Documented
@Retention(RUNTIME)
@Target({ ANNOTATION_TYPE, FIELD, METHOD, PARAMETER, TYPE_USE })
@AConstraint
public @interface VLuhn {

    /**
     * Message.
     *
     * @return message
     * @see ConstraintMetadata.Attributes#MESSAGE
     */
    String message() default "[`VLuhn`]";

    /**
     * Order
     *
     * @return order
     * @see ConstraintMetadata.Attributes#ORDER
     */
    int order() default 0;

    // #################### extra #######################################################

    /** Built-in criterion factories. */
    List<CriterionFactory<VLuhn, ?, ?>> CRITERION_FACTORIES = new LinkedList<>(Collections.singletonList(
            VLuhnCriterionFactoryForCharSequence.INSTANCE
    ));

}
