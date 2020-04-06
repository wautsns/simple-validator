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

import com.github.wautsns.templatemessage.formatter.Formatter;
import com.github.wautsns.templatemessage.formatter.common.EnumFormatter;
import com.github.wautsns.templatemessage.formatter.multival.ArrayFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Formatters.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("rawtypes")
public class Formatters {

    /**
     * enums properties(no restrictions if the enum properties are {@code null} or empty)
     *
     * <pre>
     * Human {
     *     Gender { MALE, FEMALE }
     * }
     * // exampleA
     * Human.Gender[] enumsA = Human.Gender.values();
     * ==&gt; [`Human$Gender.MALE`, `Human$Gender.MALE`]
     * // exampleB
     * Human.Gender[] enumsB = null;
     * ==&gt; [`NO_RESTRICTIONS`]
     *
     * </pre>
     */
    public static final Formatter<Enum[]> ENUMS_PROPERTIES_NO_RESTRICTIONS = new ArrayFormatter<Enum[], Enum>()
            .setStringFormatOfNull(PropertyPhrases.NO_RESTRICTIONS)
            .setStringFormatOfEmptyArray(PropertyPhrases.NO_RESTRICTIONS)
            .setComponentPrefix(ValidationFailureFormatter.LEFT_DELIMITER_RESOURCE)
            .setComponentSuffix(ValidationFailureFormatter.RIGHT_DELIMITER_RESOURCE)
            .setComponentFormatter(new EnumFormatter().setDisplayClass(true));

    /**
     * enums properties(no optional value if the enum properties are {@code null} or empty)
     *
     * <pre>
     * Human {
     *     Gender { MALE, FEMALE }
     * }
     * // exampleA
     * Human.Gender[] enumsA = Human.Gender.values();
     * ==&gt; [`Human$Gender.MALE`, `Human$Gender.MALE`]
     * // exampleB
     * Human.Gender[] enumsB = null;
     * ==&gt; [`NO_OPTIONAL_VALUE`]
     *
     * </pre>
     */
    public static final Formatter<Enum[]> ENUMS_PROPERTIES_NO_OPTIONAL_VALUE = new ArrayFormatter<Enum[], Enum>()
            .setStringFormatOfNull(PropertyPhrases.NO_OPTIONAL_VALUE)
            .setStringFormatOfEmptyArray(PropertyPhrases.NO_OPTIONAL_VALUE)
            .setComponentPrefix(ValidationFailureFormatter.LEFT_DELIMITER_RESOURCE)
            .setComponentSuffix(ValidationFailureFormatter.RIGHT_DELIMITER_RESOURCE)
            .setComponentFormatter(new EnumFormatter().setDisplayClass(true));

    /**
     * object values(no restrictions if the object values are {@code null} or empty)
     *
     * <pre>
     * // exampleA
     * String[] valuesA = new String[]{ "A", "B" };
     * ==&gt; [A, B]
     * // exampleB
     * String[] valuesA = new String[]{};
     * ==&gt; [`NO_RESTRICTIONS`]
     *
     * </pre>
     */
    public static final Formatter<Object[]> VALUES_NO_RESTRICTIONS = new ArrayFormatter<Object[], Object>()
            .setStringFormatOfNull(PropertyPhrases.NO_RESTRICTIONS)
            .setStringFormatOfEmptyArray(PropertyPhrases.NO_RESTRICTIONS);

    /**
     * object values(no optional value if the object values are {@code null} or empty)
     *
     * <pre>
     * // exampleA
     * String[] valuesA = new String[]{ "A", "B" };
     * ==&gt; [A, B]
     * // exampleB
     * String[] valuesA = new String[]{};
     * ==&gt; [`NO_OPTIONAL_VALUE`]
     *
     * </pre>
     */
    public static final Formatter<Object[]> VALUES_NO_OPTIONAL_VALUE = new ArrayFormatter<Object[], Object>()
            .setStringFormatOfNull(PropertyPhrases.NO_OPTIONAL_VALUE)
            .setStringFormatOfEmptyArray(PropertyPhrases.NO_OPTIONAL_VALUE);

}
