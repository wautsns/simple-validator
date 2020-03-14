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
package com.github.wautsns.simplevalidator.model.criterion.factory.typelike.text;

import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import com.github.wautsns.templatemessage.formatter.Formatter;

import java.lang.reflect.Type;

/**
 * Utility for {@code char[]} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class CharArrayUtility extends TextLikeUtility<char[]> {

    /** default {@code CharArrayUtility} */
    public static final CharArrayUtility DEFAULT = new CharArrayUtility();

    protected CharArrayUtility() {}

    public CharArrayUtility(Formatter<? super char[]> formatter) {
        super(formatter);
    }

    @Override
    public boolean appliesTo(Type type) {
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
