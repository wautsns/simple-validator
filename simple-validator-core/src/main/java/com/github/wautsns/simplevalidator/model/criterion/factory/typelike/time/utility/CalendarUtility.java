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
import java.util.Calendar;

/**
 * Utility for {@code Calendar} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class CalendarUtility extends TimeLikeUtility<Calendar> {

    /** Default {@code CalendarUtility}. */
    public static final CalendarUtility DEFAULT = new CalendarUtility(null);

    public CalendarUtility(Formatter<Calendar> valueFormatter) {
        super(valueFormatter);
    }

    @Override
    public boolean appliesTo(Type type) {
        return TypeUtils.isAssignableTo(type, Calendar.class);
    }

    @Override
    public Calendar now() {
        return Calendar.getInstance();
    }

    @Override
    public Calendar now(long millisecondsOffset) {
        return getInstance(System.currentTimeMillis() + millisecondsOffset);
    }

    @Override
    public Calendar now(long monthsOffset, long millisecondsOffset) {
        return getInstance(System.currentTimeMillis() + millisecondsOffset, monthsOffset);
    }

    @Override
    public Calendar plusMilliseconds(Calendar time, long milliseconds) {
        return getInstance(time.getTimeInMillis() + milliseconds);
    }

    @Override
    public Calendar plusMonths(Calendar time, long months) {
        return getInstance(time.getTimeInMillis(), months);
    }

    @Override
    public Calendar plusMonthsAndMilliseconds(Calendar time, long months, long milliseconds) {
        return getInstance(time.getTimeInMillis() + milliseconds, months);
    }

    /**
     * Plus and set months to the specified time.
     *
     * @param timestamp timestamp
     * @return {@code Calendar} instance
     */
    public Calendar getInstance(long timestamp) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timestamp);
        return instance;
    }

    /**
     * Plus and set months to the specified time.
     *
     * @param timestamp timestamp
     * @param monthsOffset months offset
     * @return {@code Calendar} instance
     */
    public Calendar getInstance(long timestamp, long monthsOffset) {
        Calendar instance = getInstance(timestamp);
        long month = instance.get(Calendar.MONTH) + monthsOffset;
        if (month > 12) {
            long year = instance.get(Calendar.YEAR) + month / 12;
            instance.set(Calendar.YEAR, (int) year);
            month %= 12;
            if (month == 0) { return instance; }
        }
        instance.set(Calendar.MONTH, (int) month);
        return instance;
    }

    @Override
    public Duration between(Calendar timeA, Calendar timeB) {
        return Duration.ofMillis(timeA.getTimeInMillis() - timeB.getTimeInMillis());
    }

    @Override
    public int compare(Calendar timeA, Calendar timeB) {
        return timeA.compareTo(timeB);
    }

}
