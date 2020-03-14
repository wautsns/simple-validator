/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.wautsns.simplevalidator.constraint.number.domain;

import com.github.wautsns.simplevalidator.util.common.NumericTextUtils;
import com.github.wautsns.simplevalidator.util.function.BytePredicate;
import com.github.wautsns.simplevalidator.util.function.FloatPredicate;
import com.github.wautsns.simplevalidator.util.function.ShortPredicate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class DomainUtils {

    public static Domain init(String[] domainTexts) {
        Domain domain = new Domain();
        for (String domainText : domainTexts) {
            DomainUnit unit = init(domainText);
            if (unit != null) { domain.units.add(unit); }
        }
        return domain;
    }

    private static DomainUnit init(String domainUnitText) {
        domainUnitText = domainUnitText.replaceAll("\\s", "");
        int lastIndex = domainUnitText.length() - 1;
        char left = domainUnitText.charAt(0);
        char right = domainUnitText.charAt(lastIndex);
        String data = domainUnitText.substring(1, lastIndex);
        if (left == '{' && right == '}') {
            return new NumericValues(data.split(","));
        } else if ("[(".indexOf(left) < 0 || ")]".indexOf(right) < 0) {
            // TODO message
            throw new IllegalArgumentException();
        } else {
            String[] tmp = data.split(",", 2);
            RangePartUnit min = null;
            RangePartUnit max = null;
            if (!"~".equals(tmp[0])) {
                min = new RangePartUnit(tmp[0], left == '[', true);
            }
            if (!"~".equals(tmp[1])) {
                max = new RangePartUnit(tmp[1], right == ']', false);
            }
            if (min == null) {
                return max;
            } else if (max == null) {
                return min;
            } else {
                return new RangeUnit(min, max);
            }
        }
    }

    public static class Domain implements DomainUnit {

        private final List<DomainUnit> units = new LinkedList<>();

        public Domain merge(Domain domain) {
            this.units.addAll(domain.units);
            return this;
        }

        @Override
        public Predicate<CharSequence> forNumericText() {
            return units.stream()
                    .map(DomainUnit::forNumericText)
                    .reduce(Predicate::or)
                    .orElse(null);
        }

        @Override
        public <T extends Number & Comparable<T>> Predicate<T> forComparableNumber(
                Class<T> clazz) {
            return units.stream()
                    .map(unit -> unit.forComparableNumber(clazz))
                    .reduce(Predicate::or)
                    .orElse(null);
        }

        @Override
        public IntPredicate forInt() {
            return units.stream()
                    .map(DomainUnit::forInt)
                    .reduce(IntPredicate::or)
                    .orElse(null);
        }

        @Override
        public LongPredicate forLong() {
            return units.stream()
                    .map(DomainUnit::forLong)
                    .reduce(LongPredicate::or)
                    .orElse(null);
        }

        @Override
        public DoublePredicate forDouble() {
            return units.stream()
                    .map(DomainUnit::forDouble)
                    .reduce(DoublePredicate::or)
                    .orElse(null);
        }

        @Override
        public BytePredicate forByte() {
            return units.stream()
                    .map(DomainUnit::forByte)
                    .reduce(BytePredicate::or)
                    .orElse(null);
        }

        @Override
        public ShortPredicate forShort() {
            return units.stream()
                    .map(DomainUnit::forShort)
                    .reduce(ShortPredicate::or)
                    .orElse(null);
        }

        @Override
        public FloatPredicate forFloat() {
            return units.stream()
                    .map(DomainUnit::forFloat)
                    .reduce(FloatPredicate::or)
                    .orElse(null);
        }

    }

    private interface DomainUnit {

        Predicate<CharSequence> forNumericText();

        <T extends Number & Comparable<T>> Predicate<T> forComparableNumber(Class<T> clazz);

        IntPredicate forInt();

        LongPredicate forLong();

        DoublePredicate forDouble();

        BytePredicate forByte();

        ShortPredicate forShort();

        FloatPredicate forFloat();

    }

    private static class NumericValues implements DomainUnit {

        private final String[] data;

        public NumericValues(String[] data) {
            this.data = data;
        }

        @Override
        public Predicate<CharSequence> forNumericText() {
            Set<BigDecimal> values = Arrays.stream(data)
                    .map(BigDecimal::new)
                    .collect(Collectors.toSet());
            return value -> {
                BigDecimal bigDecimal;
                if (value instanceof String) {
                    bigDecimal = new BigDecimal((String) value);
                } else {
                    char[] chars = new char[value.length()];
                    for (int i = 0; i < chars.length; i++) {
                        chars[i] = value.charAt(i);
                    }
                    bigDecimal = new BigDecimal(chars);
                }
                return values.contains(bigDecimal);
            };
        }

        @Override
        public <T extends Number & Comparable<T>> Predicate<T> forComparableNumber(
                Class<T> clazz) {
            List<T> refs = Arrays.stream(data)
                    .map(text -> NumericTextUtils.parse(text, clazz))
                    .sorted()
                    .collect(Collectors.toCollection(LinkedList::new));
            return value -> (Collections.binarySearch(refs, value) >= 0);
        }

        @Override
        public IntPredicate forInt() {
            int[] refs = Arrays.stream(data)
                    .mapToInt(Integer::parseInt).sorted().distinct().toArray();
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

        @Override
        public LongPredicate forLong() {
            long[] refs = Arrays.stream(data)
                    .mapToLong(Long::parseLong).sorted().distinct().toArray();
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

        @Override
        public DoublePredicate forDouble() {
            double[] refs = Arrays.stream(data)
                    .mapToDouble(Double::parseDouble).sorted().distinct().toArray();
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

        @Override
        public BytePredicate forByte() {
            byte[] refs = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                refs[i] = Byte.parseByte(data[i]);
            }
            Arrays.sort(refs);
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

        @Override
        public ShortPredicate forShort() {
            short[] refs = new short[data.length];
            for (int i = 0; i < data.length; i++) {
                refs[i] = Short.parseShort(data[i]);
            }
            Arrays.sort(refs);
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

        @Override
        public FloatPredicate forFloat() {
            float[] refs = new float[data.length];
            for (int i = 0; i < data.length; i++) {
                refs[i] = Float.parseFloat(data[i]);
            }
            Arrays.sort(refs);
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

    }

    private static class RangePartUnit implements DomainUnit {

        private final String data;
        private final boolean inclusive;
        private final boolean left;

        public RangePartUnit(String data, boolean inclusive, boolean left) {
            this.data = data;
            this.inclusive = inclusive;
            this.left = left;
        }

        @Override
        public Predicate<CharSequence> forNumericText() {
            BigDecimal ref = new BigDecimal(data);
            boolean inclusive = this.inclusive;
            boolean left = this.left;
            return value -> {
                BigDecimal bigDecimal;
                if (value instanceof String) {
                    bigDecimal = new BigDecimal((String) value);
                } else {
                    char[] chars = new char[value.length()];
                    for (int i = 0; i < chars.length; i++) {
                        chars[i] = value.charAt(i);
                    }
                    bigDecimal = new BigDecimal(chars);
                }
                int rst = ref.compareTo(bigDecimal);
                return (rst == 0) ? inclusive : ((rst > 0) ^ left);
            };
        }

        @Override
        public <T extends Number & Comparable<T>> Predicate<T> forComparableNumber(
                Class<T> clazz) {
            T ref = NumericTextUtils.parse(data, clazz);
            boolean inclusive = this.inclusive;
            boolean left = this.left;
            return value -> {
                int rst = ref.compareTo(value);
                return (rst == 0) ? inclusive : ((rst > 0) ^ left);
            };
        }

        @Override
        public IntPredicate forInt() {
            int ref = Integer.parseInt(data);
            boolean inclusive = this.inclusive;
            boolean left = this.left;
            return value -> (value == ref) ? inclusive : ((value < ref) ^ left);
        }

        @Override
        public LongPredicate forLong() {
            long ref = Long.parseLong(data);
            boolean inclusive = this.inclusive;
            boolean left = this.left;
            return value -> (value == ref) ? inclusive : ((value < ref) ^ left);
        }

        @Override
        public DoublePredicate forDouble() {
            double ref = Double.parseDouble(data);
            boolean inclusive = this.inclusive;
            boolean left = this.left;
            return value -> (value == ref) ? inclusive : ((value < ref) ^ left);
        }

        @Override
        public BytePredicate forByte() {
            byte ref = Byte.parseByte(data);
            boolean inclusive = this.inclusive;
            boolean left = this.left;
            return value -> (value == ref) ? inclusive : ((value < ref) ^ left);
        }

        @Override
        public ShortPredicate forShort() {
            short ref = Short.parseShort(data);
            boolean inclusive = this.inclusive;
            boolean left = this.left;
            return value -> (value == ref) ? inclusive : ((value < ref) ^ left);
        }

        @Override
        public FloatPredicate forFloat() {
            float ref = Float.parseFloat(data);
            boolean inclusive = this.inclusive;
            boolean left = this.left;
            return value -> (value == ref) ? inclusive : ((value < ref) ^ left);
        }

    }

    private static class RangeUnit implements DomainUnit {

        private final RangePartUnit min;
        private final RangePartUnit max;

        public RangeUnit(RangePartUnit min, RangePartUnit max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public Predicate<CharSequence> forNumericText() {
            return min.forNumericText().and(max.forNumericText());
        }

        @Override
        public <T extends Number & Comparable<T>> Predicate<T> forComparableNumber(
                Class<T> clazz) {
            return min.forComparableNumber(clazz).and(max.forComparableNumber(clazz));
        }

        @Override
        public IntPredicate forInt() {
            return min.forInt().and(max.forInt());
        }

        @Override
        public LongPredicate forLong() {
            return min.forLong().and(max.forLong());
        }

        @Override
        public DoublePredicate forDouble() {
            return min.forDouble().and(max.forDouble());
        }

        @Override
        public BytePredicate forByte() {
            return min.forByte().and(max.forByte());
        }

        @Override
        public ShortPredicate forShort() {
            return min.forShort().and(max.forShort());
        }

        @Override
        public FloatPredicate forFloat() {
            return min.forFloat().and(max.forFloat());
        }

    }

}
