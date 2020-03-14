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
package com.github.wautsns.simplevalidator.model.failure;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class PropertyPhrases {

    public static final String NO_RESTRICTIONS = wrap("NO_RESTRICTIONS");
    public static final String NO_OPTIONAL_VALUE = wrap("NO_OPTIONAL_VALUE");

    private static String wrap(String name) {
        return ValidationFailureFormatter.LEFT_DELIMITER_PROPERTIES
                + name
                + ValidationFailureFormatter.RIGHT_DELIMITER_PROPERTIES;
    }

    private PropertyPhrases() {}

}