package com.github.wautsns.simplevalidator.constraint.business.idcard;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.model.criterion.factory.CriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.Formatters;
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
public @interface VChineseIdCard {

    List<CriterionFactory<VChineseIdCard, ?, ?>> CRITERION_FACTORIES = new LinkedList<>(Collections.singletonList(
            new VChineseIdCardTypeExtendsCharSequenceCriterionFactory()
    ));

    String message() default "[`VChineseIdCard`]";

    int order() default 0;

    Generation[] generations() default {};

    String[] cities() default {};

    String[] ages() default {};

    Gender[] genders() default {};

    // ------------------------- enum ----------------------------------------------

    enum Generation {FIRST, SECOND}

    enum Gender {MALE, FEMALE}

    // ------------------------- variables -----------------------------------------

    Variable<Generation[]> GENERATIONS = new Variable<>("generations", Formatters.ENUMS_PROPERTIES_NO_RESTRICTIONS);
    Variable<String[]> CITIES = new Variable<>("cities", Formatters.VALUES_NO_RESTRICTIONS);
    Variable<String[]> AGES = new Variable<>("ages", Formatters.VALUES_NO_RESTRICTIONS);
    Variable<Gender[]> GENDERS = new Variable<>("genders", Formatters.ENUMS_PROPERTIES_NO_RESTRICTIONS);

}
