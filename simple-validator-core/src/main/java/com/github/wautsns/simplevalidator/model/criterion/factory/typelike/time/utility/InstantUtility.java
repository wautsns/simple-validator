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
import java.time.Instant;

/**
 * Utility for {@code Instant} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class InstantUtility extends TimeLikeUtility<Instant> {

    /** default {@code InstantUtility} */
    public static final InstantUtility DEFAULT = new InstantUtility(null);

    public InstantUtility(Formatter<Instant> valueFormatter) {
        super(valueFormatter);
    }

    @Override
    public boolean appliesTo(Type type) {
        return TypeUtils.isAssignableTo(type, Instant.class);
    }

    @Override
    public Instant now() {
        return Instant.now();
    }

    @Override
    public Instant now(long millisecondsOffset) {
        return Instant.ofEpochMilli(System.currentTimeMillis() + millisecondsOffset);
    }

    @Override
    public Instant now(long monthsOffset, long millisecondsOffset) {
        long timestamp = CalendarUtility.DEFAULT
                .now(monthsOffset, millisecondsOffset)
                .getTimeInMillis();
        return Instant.ofEpochMilli(timestamp);
    }

    @Override
    public Instant plusMilliseconds(Instant time, long milliseconds) {
        return time.plusMillis(milliseconds);
    }

    @Override
    public Instant plusMonths(Instant time, long months) {
        long timestamp = CalendarUtility.DEFAULT
                .getInstance(time.toEpochMilli(), months)
                .getTimeInMillis();
        return Instant.ofEpochMilli(timestamp);
    }

    @Override
    public Instant plusMonthsAndMilliseconds(Instant time, long months, long milliseconds) {
        long timestamp = CalendarUtility.DEFAULT
                .getInstance(time.toEpochMilli() + milliseconds, months)
                .getTimeInMillis();
        return Instant.ofEpochMilli(timestamp);
    }

    @Override
    public Duration between(Instant timeA, Instant timeB) {
        return Duration.between(timeA, timeB);
    }

    @Override
    public int compare(Instant timeA, Instant timeB) {
        return timeA.compareTo(timeB);
    }

}
