/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wautsns.simplevalidator.exception;

import com.github.wautsns.simplevalidator.SimpleValidatorConfiguration;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;

/**
 * Validation exception.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -5706334622805333445L;

    /**
     * Construct a validationException.
     *
     * @param validationFailure validation failure
     */
    public ValidationException(ValidationFailure validationFailure) {
        super(SimpleValidatorConfiguration.ForValidationFailure.FORMATTER.format(
                validationFailure, SimpleValidatorConfiguration.ForValidationFailure.getLocaleSupplier().get()));
    }

    /**
     * Construct a validationException.
     *
     * @param message message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Construct a validationException.
     *
     * @param message message
     * @param cause cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
