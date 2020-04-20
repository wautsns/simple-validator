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
import java.util.Date;

/**
 * Utility for {@code Date} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class DateUtility extends TimeLikeUtility<Date> {

    /** default {@code DateUtility} */
    public static final DateUtility DEFAULT = new DateUtility(null);

    public DateUtility(Formatter<? super Date> valueFormatter) {
        super(valueFormatter);
    }

    @Override
    public boolean appliesTo(Type type) {
        return TypeUtils.isAssignableTo(type, Date.class);
    }

    @Override
    public Date now() {
        return new Date();
    }

    @Override
    public Date now(long millisecondsOffset) {
        return new Date(System.currentTimeMillis() + millisecondsOffset);
    }

    @Override
    public Date now(long monthsOffset, long millisecondsOffset) {
        return CalendarUtility.DEFAULT
                .now(monthsOffset, millisecondsOffset)
                .getTime();
    }

    @Override
    public Date plusMilliseconds(Date time, long milliseconds) {
        return new Date(time.getTime() + milliseconds);
    }

    @Override
    public Date plusMonths(Date time, long months) {
        return CalendarUtility.DEFAULT
                .getInstance(time.getTime(), months)
                .getTime();
    }

    @Override
    public Date plusMonthsAndMilliseconds(Date time, long months, long milliseconds) {
        return CalendarUtility.DEFAULT
                .getInstance(time.getTime() + milliseconds, months)
                .getTime();
    }

    @Override
    public Duration between(Date timeA, Date timeB) {
        return Duration.ofMillis(timeA.getTime() - timeB.getTime());
    }

    @Override
    public int compare(Date timeA, Date timeB) {
        return timeA.compareTo(timeB);
    }

}
