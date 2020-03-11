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

import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.TypeLikeUtility;
import com.github.wautsns.templatemessage.formatter.Formatter;

/**
 * Text like utility.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class TextLikeUtility<T> extends TypeLikeUtility<T> {

    protected TextLikeUtility() {}

    public TextLikeUtility(Formatter<? super T> formatter) {
        super(formatter);
    }

    /**
     * Return length of the text.
     *
     * @param text text
     * @return length of the text
     */
    public abstract int length(T text);

    /**
     * Return the char value at the specific index.
     *
     * @param text text value
     * @param index the index of the char value
     * @return the char value at the specific index.
     */
    public abstract char charAt(T text, int index);

    /**
     * Convert the text to {@code CharSequence} value.
     *
     * @param text text
     * @return {@code CharSequence} value
     */
    public abstract CharSequence toCharSequence(T text);

}
