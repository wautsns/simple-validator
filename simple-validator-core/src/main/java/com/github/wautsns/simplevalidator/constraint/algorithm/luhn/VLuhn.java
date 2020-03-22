package com.github.wautsns.simplevalidator.constraint.algorithm.luhn;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;
import com.github.wautsns.simplevalidator.util.extractor.ValueExtractor;

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
 * @since Mar 19, 2020
 */
@Documented
@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER, TYPE_USE})
@AConstraint
public @interface VLuhn {

    String message() default "[`VLuhn`]";

    int order() default 0;

    // -------------------- metadata ----------------------------------------------------

    List<CriterionFactory<VLuhn, ?, ?>> CRITERION_FACTORY_LIST = new LinkedList<>(Collections.singletonList(
            VLuhnCharSequenceCriterionFactory.INSTANCE
    ));

    List<ValueExtractor> VALUE_EXTRACTOR_LIST = new LinkedList<>();

}
