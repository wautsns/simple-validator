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
package com.github.wautsns.templatemessage.variable;

import com.github.wautsns.templatemessage.formatter.Formatter;
import com.github.wautsns.templatemessage.formatter.common.ObjectFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Variable.
 *
 * @param <T> type of value of variable
 * @author wautsns
 * @since Mar 10, 2020
 */
@Getter
@EqualsAndHashCode(of = "name")
public class Variable<T> implements Serializable {

    private static final long serialVersionUID = 2161734187241083633L;

    /** name of variable */
    private final String name;
    /** formatter of value of variable */
    private final Formatter<? super T> formatter;

    @Override
    public String toString() {
        return name;
    }

    // #################### constructor ##################################################

    /**
     * Construct a variable with {@linkplain ObjectFormatter#DEFAULT default formatter}.
     *
     * @param name name of variable
     */
    public Variable(String name) {
        this(name, ObjectFormatter.DEFAULT);
    }

    /**
     * Construct a variable.
     *
     * @param name variable name
     * @param formatter value formatter
     */
    public Variable(String name, Formatter<? super T> formatter) {
        this.name = Objects.requireNonNull(name);
        this.formatter = Objects.requireNonNull(formatter);
    }

}
