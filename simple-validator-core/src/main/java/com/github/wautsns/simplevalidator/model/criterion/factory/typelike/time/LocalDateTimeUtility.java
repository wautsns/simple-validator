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
package com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time;

import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import com.github.wautsns.templatemessage.formatter.Formatter;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class LocalDateTimeUtility extends TimeLikeUtility<LocalDateTime> {

    /** default {@code LocalDateTimeUtility} */
    public static final LocalDateTimeUtility DEFAULT = new LocalDateTimeUtility();

    public LocalDateTimeUtility() {}

    public LocalDateTimeUtility(Formatter<LocalDateTime> formatter) {
        super(formatter);
    }

    @Override
    public boolean appliesTo(Type type) {
        return TypeUtils.isAssignableTo(type, LocalDateTime.class);
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDateTime now(long milliseconds) {
        return LocalDateTime.now()
                .plus(milliseconds, ChronoUnit.MILLIS);
    }

    @Override
    public LocalDateTime now(long years, long months, long milliseconds) {
        return LocalDateTime.now()
                .plusYears(years)
                .plusMonths(months)
                .plus(milliseconds, ChronoUnit.MILLIS);
    }

    @Override
    public int compare(LocalDateTime timeA, LocalDateTime timeB) {
        return timeA.compareTo(timeB);
    }

}
