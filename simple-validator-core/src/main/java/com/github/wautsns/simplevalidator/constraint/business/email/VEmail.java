package com.github.wautsns.simplevalidator.constraint.business.email;

import com.github.wautsns.simplevalidator.constraint.AConstraint;
import com.github.wautsns.simplevalidator.constraint.text.pattern.VPattern;

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
@VPattern(regex = VEmail.REGEX)
public @interface VEmail {

    String REGEX = "[a-zA-Z0-9-]+@[a-zA-z0-9-]+([.][a-zA-z0-9-]+)+";

    /**
     * Message(template).
     *
     * @return message(template)
     */
    String message() default "[`VEmail`]";

    /**
     * Order of the constraint.
     *
     * @return order of the constraint
     */
    int order() default 0;

}
