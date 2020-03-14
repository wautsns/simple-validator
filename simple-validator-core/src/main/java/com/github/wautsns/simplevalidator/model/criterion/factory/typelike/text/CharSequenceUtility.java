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
 * Utility for {@code CharSequence} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class CharSequenceUtility extends TextLikeUtility<CharSequence> {

    /** default {@code CharSequenceUtility} */
    public static final CharSequenceUtility DEFAULT = new CharSequenceUtility();

    protected CharSequenceUtility() {}

    public CharSequenceUtility(Formatter<? super CharSequence> formatter) {
        super(formatter);
    }

    @Override
    public boolean appliesTo(Type type) {
        return TypeUtils.isAssignableTo(type, CharSequence.class);
    }

    @Override
    public int length(CharSequence text) {
        return text.length();
    }

    @Override
    public char charAt(CharSequence text, int index) {
        return text.charAt(index);
    }

    @Override
    public CharSequence toCharSequence(CharSequence text) {
        return text;
    }

}
