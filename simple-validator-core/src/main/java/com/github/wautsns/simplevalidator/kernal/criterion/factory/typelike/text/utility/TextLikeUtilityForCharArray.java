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
package com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.text.utility;

import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.text.TextLikeUtility;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import com.github.wautsns.templatemessage.formatter.Formatter;

import java.lang.reflect.Type;

/**
 * Text like utility for {@code char[]} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class TextLikeUtilityForCharArray extends TextLikeUtility<char[]> {

    /** Default {@code TextLikeUtilityForCharArray}. */
    public static final TextLikeUtilityForCharArray DEFAULT = new TextLikeUtilityForCharArray(null);

    /**
     * Construct a char array utility.
     *
     * @param formatter formatter
     */
    public TextLikeUtilityForCharArray(Formatter<? super char[]> formatter) {
        super(formatter);
    }

    @Override
    public boolean applyTo(Type type) {
        return TypeUtils.isAssignableTo(type, char[].class);
    }

    @Override
    public int length(char[] text) {
        return text.length;
    }

    @Override
    public char charAt(char[] text, int index) {
        return text[index];
    }

    @Override
    public CharSequence toCharSequence(char[] text) {
        return new String(text);
    }

}
