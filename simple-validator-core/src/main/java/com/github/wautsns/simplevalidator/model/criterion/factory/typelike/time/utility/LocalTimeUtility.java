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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Utility for {@code LocalTime} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class LocalTimeUtility extends TimeLikeUtility<LocalTime> {

    /** Default {@code LocalTimeUtility}. */
    public static final LocalTimeUtility DEFAULT = new LocalTimeUtility(null);

    public LocalTimeUtility(Formatter<LocalTime> valueFormatter) {
        super(valueFormatter);
    }

    @Override
    public boolean appliesTo(Type type) {
        return TypeUtils.isAssignableTo(type, LocalTime.class);
    }

    @Override
    public LocalTime now() {
        return LocalTime.now();
    }

    @Override
    public LocalTime now(long millisecondsOffset) {
        return plusMilliseconds(now(), millisecondsOffset);
    }

    @Override
    public LocalTime now(long monthsOffset, long millisecondsOffset) {
        return now(millisecondsOffset);
    }

    @Override
    public LocalTime plusMilliseconds(LocalTime time, long milliseconds) {
        return time.plus(milliseconds, ChronoUnit.MILLIS);
    }

    @Override
    public LocalTime plusMonths(LocalTime time, long months) {
        return time;
    }

    @Override
    public LocalTime plusMonthsAndMilliseconds(LocalTime time, long months, long milliseconds) {
        return plusMilliseconds(time, milliseconds);
    }

    @Override
    public Duration between(LocalTime timeA, LocalTime timeB) {
        return Duration.between(timeA, timeB);
    }

    @Override
    public int compare(LocalTime timeA, LocalTime timeB) {
        return timeA.compareTo(timeB);
    }

}
