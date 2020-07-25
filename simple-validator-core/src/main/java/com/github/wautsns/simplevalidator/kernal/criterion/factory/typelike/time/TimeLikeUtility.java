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
package com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.time;

import com.github.wautsns.simplevalidator.kernal.criterion.factory.typelike.TypeLikeUtility;
import com.github.wautsns.templatemessage.formatter.Formatter;

import java.time.Duration;

/**
 * Time-like utility.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class TimeLikeUtility<T> extends TypeLikeUtility<T> {

    /**
     * Construct a time-like utility.
     *
     * @param formatter value formatter
     */
    public TimeLikeUtility(Formatter<? super T> formatter) {
        super(formatter);
    }

    /**
     * Get current time.
     *
     * @return the current time
     */
    public abstract T now();

    /**
     * Get current time with offset.
     *
     * @param millisecondsOffset milliseconds offset
     * @return current time with offset
     */
    public abstract T now(long millisecondsOffset);

    /**
     * Get current time with offset.
     *
     * @param monthsOffset months offset
     * @param millisecondsOffset milliseconds offset
     * @return current time with offset
     */
    public abstract T now(long monthsOffset, long millisecondsOffset);

    /**
     * Get current time with offset.
     *
     * @param yearsOffset years offset
     * @param monthsOffset months offset
     * @param millisecondsOffset milliseconds offset
     * @return current time with offset
     */
    public T now(long yearsOffset, long monthsOffset, long millisecondsOffset) {
        return now(yearsOffset * 12, millisecondsOffset);
    }

    /**
     * Return a copy of time with the specified number of milliseconds added.
     *
     * @param time time
     * @param milliseconds milliseconds
     * @return a copy of time with the specified number of milliseconds added
     */
    public abstract T plusMilliseconds(T time, long milliseconds);

    /**
     * Return a copy of time with the specified number of months added.
     *
     * @param time time
     * @param months months
     * @return a copy of time with the specified number of months added
     */
    public abstract T plusMonths(T time, long months);

    /**
     * Return a copy of time with the specified number of months, milliseconds added.
     *
     * @param time time
     * @param months months
     * @param milliseconds milliseconds
     * @return a copy of time with the specified number of months, milliseconds added
     */
    public abstract T plusMonthsAndMilliseconds(T time, long months, long milliseconds);

    /**
     * Return a copy of time with the specified number of years, months added.
     *
     * @param time time
     * @param years years
     * @param months months
     * @return a copy of time with the specified number of years, months added
     */
    public T plusYearsAndMonths(T time, long years, long months) {
        return plusMonths(time, years * 12 + months);
    }

    /**
     * Return a copy of time with the specified number of years, months, milliseconds added.
     *
     * @param time time
     * @param years years
     * @param months months
     * @param milliseconds milliseconds
     * @return a copy of time with the specified number of years, months, milliseconds added
     */
    public T plusYearsMonthsAndMilliseconds(T time, long years, long months, long milliseconds) {
        return plusMonthsAndMilliseconds(time, years * 12 + months, milliseconds);
    }

    /**
     * Get duration between timeA and timeB.
     *
     * @param timeA timeA
     * @param timeB timeB
     * @return duration between timeA and timeB
     */
    public abstract Duration between(T timeA, T timeB);

    /**
     * Compare timeA and timeB.
     *
     * @param timeA timeA
     * @param timeB timeB
     * @return {@code 1} if timeA is before timeB, {@code -1} if timeA is after timeB, otherwise {@code 0}
     */
    public abstract int compare(T timeA, T timeB);

    /**
     * Return whether timeA is before timeB.
     *
     * @param timeA time A
     * @param timeB time B
     * @return {@code true} if timeA is before timeB, otherwise {@code false}
     */
    public boolean isBefore(T timeA, T timeB) {
        return compare(timeA, timeB) < 0;
    }

    /**
     * Return whether timeA is after timeB.
     *
     * @param timeA time A
     * @param timeB time B
     * @return {@code true} if timeA is after timeB, otherwise {@code false}
     */
    public boolean isAfter(T timeA, T timeB) {
        return compare(timeA, timeB) > 0;
    }

    // #################### utils #######################################################

    /**
     * To milliseconds.
     *
     * @param weeks weeks
     * @param days days
     * @param hours hours
     * @param minutes minutes
     * @param seconds seconds
     * @return milliseconds
     */
    public static long toMilliseconds(long weeks, long days, long hours, long minutes, long seconds) {
        return weeks * 604800_000
                + days * 86400_000
                + hours * 3600_000
                + minutes * 60_000
                + seconds * 1000;
    }

}
