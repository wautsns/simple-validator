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

/**
 * Utility for {@code Long}(milliseconds timestamp) value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class LongUtility extends TimeLikeUtility<Long> {

    /** default {@code LongUtility} */
    public static final LongUtility DEFAULT = new LongUtility(null);

    public LongUtility(Formatter<Long> valueFormatter) {
        super(valueFormatter);
    }

    @Override
    public boolean appliesTo(Type type) {
        return TypeUtils.isAssignableTo(type, Long.class);
    }

    @Override
    public Long now() {
        return System.currentTimeMillis();
    }

    @Override
    public Long now(long millisecondsOffset) {
        return System.currentTimeMillis() + millisecondsOffset;
    }

    @Override
    public Long now(long monthsOffset, long millisecondsOffset) {
        return plusMonthsAndMilliseconds(now(), monthsOffset, millisecondsOffset);
    }

    @Override
    public Long plusMilliseconds(Long time, long milliseconds) {
        return time + milliseconds;
    }

    @Override
    public Long plusMonths(Long time, long months) {
        return CalendarUtility.DEFAULT
                .getInstance(time, months)
                .getTimeInMillis();
    }

    @Override
    public Long plusMonthsAndMilliseconds(Long time, long months, long milliseconds) {
        return plusMonths(plusMilliseconds(time, milliseconds), months);
    }

    @Override
    public Duration between(Long timeA, Long timeB) {
        return Duration.ofMillis(timeA - timeB);
    }

    @Override
    public int compare(Long timeA, Long timeB) {
        return timeA.compareTo(timeB);
    }

}
