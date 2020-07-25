package com.github.wautsns.simplevalidator.constraint.business.idcard;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.basic.CriterionFactory;
import com.github.wautsns.simplevalidator.kernal.failure.Formatters;
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
 * VChineseIdCard.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@Documented
@Retention(RUNTIME)
@Target({ ANNOTATION_TYPE, FIELD, METHOD, PARAMETER, TYPE_USE })
@AConstraint
public @interface VChineseIdCard {

    /**
     * Message(template).
     *
     * @return message(template)
     */
    String message() default "[`VChineseIdCard`]";

    /**
     * Order of the constraint.
     *
     * @return order of the constraint
     */
    int order() default 0;

    Generation[] generations() default { Generation.SECOND };

    String[] cities() default {};

    String[] ages() default {};

    Gender[] genders() default {};

    // #################### extra #######################################################

    enum Generation {FIRST, SECOND}

    enum Gender {MALE, FEMALE}

    /** Built-in criterion factories. */
    List<CriterionFactory<VChineseIdCard, ?, ?>> CRITERION_FACTORIES = new LinkedList<>(Collections.singletonList(
            VChineseIdCardCriterionFactoryForCharSequence.INSTANCE
    ));

    // ==================== variables ===================================================

    /** Variables: {@linkplain #generations() generations}. */
    Variable<Generation[]> GENERATIONS = new Variable<>("generations", Formatters.ENUMS_PROPERTIES_NO_RESTRICTIONS);
    /** Variables: {@linkplain #cities() cities}. */
    Variable<String[]> CITIES = new Variable<>("cities", Formatters.VALUES_NO_RESTRICTIONS);
    /** Variables: {@linkplain #ages()} () ages}. */
    Variable<String[]> AGES = new Variable<>("ages", Formatters.VALUES_NO_RESTRICTIONS);
    /** Variables: {@linkplain #genders()} genders}. */
    Variable<Gender[]> GENDERS = new Variable<>("genders", Formatters.ENUMS_PROPERTIES_NO_RESTRICTIONS);

}
