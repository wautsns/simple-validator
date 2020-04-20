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
package com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.utility;

import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.time.TimeLikeUtility;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import com.github.wautsns.templatemessage.formatter.Formatter;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Utility for {@code LocalDate} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class LocalDateUtility extends TimeLikeUtility<LocalDate> {

    /** default {@code LocalDateUtility} */
    public static final LocalDateUtility DEFAULT = new LocalDateUtility(null);

    public LocalDateUtility(Formatter<LocalDate> valueFormatter) {
        super(valueFormatter);
    }

    @Override
    public boolean appliesTo(Type type) {
        return TypeUtils.isAssignableTo(type, LocalDate.class);
    }

    @Override
    public LocalDate now() {
        return LocalDate.now();
    }

    @Override
    public LocalDate now(long millisecondsOffset) {
        return plusMilliseconds(now(), millisecondsOffset);
    }

    @Override
    public LocalDate now(long monthsOffset, long millisecondsOffset) {
        return plusMonthsAndMilliseconds(now(), monthsOffset, millisecondsOffset);
    }

    @Override
    public LocalDate plusMilliseconds(LocalDate time, long milliseconds) {
        return time.plusDays(milliseconds / 86400_000);
    }

    @Override
    public LocalDate plusMonths(LocalDate time, long months) {
        return time.plusMonths(months);
    }

    @Override
    public LocalDate plusMonthsAndMilliseconds(LocalDate time, long months, long milliseconds) {
        return plusMonths(plusMilliseconds(time, milliseconds), months);
    }

    @Override
    public Duration between(LocalDate timeA, LocalDate timeB) {
        return null;
    }

    @Override
    public int compare(LocalDate timeA, LocalDate timeB) {
        return timeA.compareTo(timeB);
    }

}
