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

import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.TypeLikeUtility;
import com.github.wautsns.templatemessage.formatter.Formatter;

import java.time.ZoneId;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class TimeLikeUtility<T> extends TypeLikeUtility<T> {

    protected TimeLikeUtility() {}

    public TimeLikeUtility(Formatter<? super T> formatter) {
        super(formatter);
    }

    /**
     * Get the current time.
     *
     * @return the current time
     */
    public abstract T now();

    /**
     * Get the current time with offset.
     *
     * @param milliseconds millisecond offset
     * @return the current time with offset
     */
    public abstract T now(long milliseconds);

    /**
     * Get the current time with offset.
     *
     * @param years year offset
     * @param months month offset
     * @param milliseconds millisecond offset
     * @return the current time with offset
     */
    public abstract T now(long years, long months, long milliseconds);

    /**
     * Compare timeA and timeB.
     *
     * @param timeA time A
     * @param timeB time B
     * @return {@code 1} if timeA is before timeB, {@code -1} if timeA is after timeB, otherwise {@code 0}
     */
    public abstract int compare(T timeA, T timeB);

    /**
     * Return {@code true} if timeA is before timeB, otherwise {@code false}.
     *
     * @param timeA time A
     * @param timeB time B
     * @return {@code true} if timeA is before timeB, otherwise {@code false}
     */
    public boolean isBefore(T timeA, T timeB) {
        return compare(timeA, timeB) < 0;
    }

    /**
     * Return {@code true} if timeA is after timeB, otherwise {@code false}.
     *
     * @param timeA time A
     * @param timeB time B
     * @return {@code true} if timeA is after timeB, otherwise {@code false}
     */
    public boolean isAfter(T timeA, T timeB) {
        return compare(timeA, timeB) > 0;
    }

    // -------------------- static ----------------------------------

    /** the value of{@code ZoneId.systemDefault()} */
    public static final ZoneId ZONE_ID_SYSTEM = ZoneId.systemDefault();

    /**
     * Calculate milliseconds.
     *
     * @param days days
     * @param hours hours
     * @param minutes minutes
     * @param seconds seconds
     * @return milliseconds
     */
    public static long toMilliseconds(long days, long hours, long minutes, long seconds) {
        hours += days * 24;
        minutes += hours * 60;
        seconds += minutes * 60;
        return seconds * 1000;
    }

}
