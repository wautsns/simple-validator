/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.wautsns.simplevalidator.springbootstarter.handler.exception;

import com.github.wautsns.simplevalidator.exception.ValidationException;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailureFormatter;
import com.github.wautsns.templatemessage.variable.Variable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author wautsns
 * @since Mar 14, 2020
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class SimpleValidatorExceptionGlobalHandler {

    private final ValidationFailureFormatter validationFailureFormatter;

    private static final Set<Variable<?>> UNNECESSARY_VARIABLES = new HashSet<>(Arrays.asList(
            ValidationFailure.Variables.TARGET, ValidationFailure.Variables.INDICATORS
    ));

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> validationException(ValidationException exception) {
        ValidationFailure failure = exception.getFailure();
        Locale locale = LocaleContextHolder.getLocale();
        String message = validationFailureFormatter.format(failure, locale);
        HashMap<String, Object> data = new HashMap<>();
        failure.entryStream()
                .filter(e -> !UNNECESSARY_VARIABLES.contains(e.getKey()))
                .forEach(e -> data.put(e.getKey().getName(), e.getValue()));
        data.put("message", message);
        return data;
    }

}
