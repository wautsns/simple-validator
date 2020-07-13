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
package com.github.wautsns.simplevalidator.exception.analysis;

/**
 * Constraint analysis exception.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
public class ConstraintAnalysisException extends RuntimeException {

    private static final long serialVersionUID = -643722216957275514L;

    /**
     * Construct a constraintAnalysisException.
     *
     * @param cause cause
     * @param messageFormat message format
     * @param args message format args
     */
    public ConstraintAnalysisException(Throwable cause, String messageFormat, Object... args) {
        super(String.format(messageFormat, args), cause);
    }

    /**
     * Construct a constraintAnalysisException.
     *
     * @param messageFormat message format
     * @param args message format args
     */
    public ConstraintAnalysisException(String messageFormat, Object... args) {
        this(null, messageFormat, args);
    }

    /**
     * Construct a constraintAnalysisException.
     *
     * @param cause cause
     */
    public ConstraintAnalysisException(Throwable cause) {
        super(cause);
    }

}
