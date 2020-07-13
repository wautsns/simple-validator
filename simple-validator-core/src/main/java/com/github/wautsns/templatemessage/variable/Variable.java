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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Variable.
 *
 * @param <T> type of variable
 * @author wautsns
 * @since Mar 10, 2020
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class Variable<T> {

    /** Variable name. */
    private final String name;
    /** Formatter for variable value, default is {@link ObjectFormatter#DEFAULT}. */
    private Formatter<? super T> formatter = ObjectFormatter.DEFAULT;

    @Override
    public String toString() {
        return name;
    }

}
