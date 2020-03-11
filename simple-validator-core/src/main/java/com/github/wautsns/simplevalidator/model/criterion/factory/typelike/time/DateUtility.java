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

import com.github.wautsns.simplevalidator.util.normal.TypeUtils;
import com.github.wautsns.templatemessage.formatter.Formatter;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Utility for {@code Date} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class DateUtility extends TimeLikeUtility<Date> {

    /** default {@code DateUtility} */
    public static final DateUtility DEFAULT = new DateUtility();

    protected DateUtility() {}

    public DateUtility(Formatter<? super Date> formatter) {
        super(formatter);
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
    public Date now(long milliseconds) {
        return new Date(System.currentTimeMillis() + milliseconds);
    }

    @Override
    public Date now(long years, long months, long milliseconds) {
        Instant instant = LocalDateTime.ofInstant(now(milliseconds).toInstant(), ZONE_ID_SYSTEM)
                .plusYears(years).plusMonths(months)
                .atZone(ZONE_ID_SYSTEM).toInstant();
        return Date.from(instant);
    }

    @Override
    public int compare(Date timeA, Date timeB) {
        return timeA.compareTo(timeB);
    }

}
