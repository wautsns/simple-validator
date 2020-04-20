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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Utility for {@code LocalDateTime} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class LocalDateTimeUtility extends TimeLikeUtility<LocalDateTime> {

    /** default {@code LocalDateTimeUtility} */
    public static final LocalDateTimeUtility DEFAULT = new LocalDateTimeUtility(null);

    public LocalDateTimeUtility(Formatter<LocalDateTime> valueFormatter) {
        super(valueFormatter);
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
    public LocalDateTime now(long millisecondsOffset) {
        return plusMilliseconds(now(), millisecondsOffset);
    }

    @Override
    public LocalDateTime now(long monthsOffset, long millisecondsOffset) {
        return plusMonthsAndMilliseconds(now(), monthsOffset, millisecondsOffset);
    }

    @Override
    public LocalDateTime plusMilliseconds(LocalDateTime time, long milliseconds) {
        return time.plus(milliseconds, ChronoUnit.MILLIS);
    }

    @Override
    public LocalDateTime plusMonths(LocalDateTime time, long months) {
        return time.plusMonths(months);
    }

    @Override
    public LocalDateTime plusMonthsAndMilliseconds(LocalDateTime time, long months, long milliseconds) {
        return plusMonths(plusMilliseconds(time, milliseconds), months);
    }

    @Override
    public Duration between(LocalDateTime timeA, LocalDateTime timeB) {
        return Duration.between(timeA, timeB);
    }

    @Override
    public int compare(LocalDateTime timeA, LocalDateTime timeB) {
        return timeA.compareTo(timeB);
    }

}
