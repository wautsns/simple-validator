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

import com.github.wautsns.templatemessage.formatter.common.EnumFormatter;
import com.github.wautsns.templatemessage.formatter.Formatter;
import com.github.wautsns.templatemessage.formatter.multival.ArrayFormatter;
import lombok.experimental.UtilityClass;

/**
 * Formatters.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@UtilityClass
@SuppressWarnings("rawtypes")
public class Formatters {

    public static final Formatter<Enum[]> ENUMS_PROPERTIES_NO_RESTRICTIONS = new ArrayFormatter<Enum[], Enum>()
            .setStringFormatOfNull(PropertyPhrases.NO_RESTRICTIONS)
            .setStringFormatOfEmptyArray(PropertyPhrases.NO_RESTRICTIONS)
            .setComponentPrefix(ValidationFailureFormatter.LEFT_DELIMITER_PROPERTIES)
            .setComponentSuffix(ValidationFailureFormatter.RIGHT_DELIMITER_PROPERTIES)
            .setComponentFormatter(new EnumFormatter().setDisplayClass(true));

    public static final Formatter<Enum[]> ENUMS_PROPERTIES_NO_OPTIONAL_VALUE = new ArrayFormatter<Enum[], Enum>()
            .setStringFormatOfNull(PropertyPhrases.NO_OPTIONAL_VALUE)
            .setStringFormatOfEmptyArray(PropertyPhrases.NO_OPTIONAL_VALUE)
            .setComponentPrefix(ValidationFailureFormatter.LEFT_DELIMITER_PROPERTIES)
            .setComponentSuffix(ValidationFailureFormatter.RIGHT_DELIMITER_PROPERTIES)
            .setComponentFormatter(new EnumFormatter().setDisplayClass(true));

    public static final Formatter<Object[]> VALUES_NO_RESTRICTIONS = new ArrayFormatter<Object[], Object>()
            .setStringFormatOfNull(PropertyPhrases.NO_RESTRICTIONS)
            .setStringFormatOfEmptyArray(PropertyPhrases.NO_RESTRICTIONS);

    public static final Formatter<Object[]> VALUES_NO_OPTIONAL_VALUE = new ArrayFormatter<Object[], Object>()
            .setStringFormatOfNull(PropertyPhrases.NO_OPTIONAL_VALUE)
            .setStringFormatOfEmptyArray(PropertyPhrases.NO_OPTIONAL_VALUE);

}
