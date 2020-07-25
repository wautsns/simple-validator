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

import com.github.wautsns.simplevalidator.util.function.BytePredicate;
import com.github.wautsns.simplevalidator.util.function.FloatPredicate;
import com.github.wautsns.simplevalidator.util.function.ShortPredicate;
import com.github.wautsns.simplevalidator.util.valuehandle.NumericTextParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Domain utils.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public class DomainUtils {

    /**
     * Initialize {@code Domain} by specified domain texts.
     *
     * @param domainTexts domain texts
     * @return {@code Domain}
     */
    public static Domain init(String[] domainTexts) {
        Domain domain = new Domain();
        Arrays.stream(domainTexts)
                .map(DomainUtils::initDomainUnit)
                .forEach(domain.domainUnits::add);
        return domain;
    }

    /**
     * Initialize {@code DomainUnit} by domain-unit-text.
     *
     * @param domainUnitText domain unit text
     * @return {@code DomainUnit}
     */
    private static DomainUnit initDomainUnit(String domainUnitText) {
        domainUnitText = domainUnitText.replaceAll("\\s", "");
        int lastIndex = domainUnitText.length() - 1;
        char left = domainUnitText.charAt(0);
        char right = domainUnitText.charAt(lastIndex);
        String data = domainUnitText.substring(1, lastIndex);
        if (left == '{' && right == '}') {
            return new NumberSet(data.split(","));
        } else {
            return new RangeUnit(domainUnitText);
        }
    }

    /**
     * Parse numeric text to {@code BigDecimal} value.
     *
     * @param numericText numeric text
     * @return {@code BigDecimal} value
     */
    private static BigDecimal parseNumericTextToBigDecimal(CharSequence numericText) {
        if (numericText instanceof String) {
            return new BigDecimal((String) numericText);
        } else {
            char[] numericChars = new char[numericText.length()];
            for (int i = 0; i < numericChars.length; i++) {
                numericChars[i] = numericText.charAt(i);
            }
            return new BigDecimal(numericChars);
        }
    }

    /** Domain. */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Domain implements DomainUnit {

        /** {@code DomainUnit} list. */
        private final List<DomainUnit> domainUnits = new LinkedList<>();

        /**
         * Merge another domain.
         *
         * @param domain domain
         */
        public void merge(Domain domain) {
            this.domainUnits.addAll(domain.domainUnits);
        }

        @Override
        public Predicate<CharSequence> initPredicateForNumericText() {
            Predicate<BigDecimal> predicate = initPredicateForComparableNumber(BigDecimal.class);
            if (predicate == null) { return null; }
            return value -> {
                BigDecimal bigDecimalValue;
                if (value instanceof String) {
                    bigDecimalValue = new BigDecimal((String) value);
                } else {
                    char[] chars = new char[value.length()];
                    for (int i = 0; i < chars.length; i++) {
                        chars[i] = value.charAt(i);
                    }
                    bigDecimalValue = new BigDecimal(chars);
                }
                return predicate.test(bigDecimalValue);
            };
        }

        @Override
        public <T extends Number & Comparable<T>> Predicate<T> initPredicateForComparableNumber(Class<T> type) {
            List<Predicate<T>> predicates = domainUnits.stream()
                    .map(unit -> unit.initPredicateForComparableNumber(type))
                    .collect(Collectors.toCollection(LinkedList::new));
            if (predicates.isEmpty()) { return null; }
            if (predicates.size() == 1) { return predicates.get(0); }
            return value -> {
                for (Predicate<T> predicate : predicates) {
                    if (!predicate.test(value)) {
                        return false;
                    }
                }
                return true;
            };
        }

        @Override
        public IntPredicate initPredicateForInt() {
            List<IntPredicate> predicates = domainUnits.stream()
                    .map(DomainUnit::initPredicateForInt)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (predicates.isEmpty()) { return null; }
            if (predicates.size() == 1) { return predicates.get(0); }
            return value -> {
                for (IntPredicate predicate : predicates) {
                    if (!predicate.test(value)) {
                        return false;
                    }
                }
                return true;
            };
        }

        @Override
        public LongPredicate initPredicateForLong() {
            List<LongPredicate> predicates = domainUnits.stream()
                    .map(DomainUnit::initPredicateForLong)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (predicates.isEmpty()) { return null; }
            if (predicates.size() == 1) { return predicates.get(0); }
            return value -> {
                for (LongPredicate predicate : predicates) {
                    if (!predicate.test(value)) {
                        return false;
                    }
                }
                return true;
            };
        }

        @Override
        public DoublePredicate initPredicateForDouble() {
            List<DoublePredicate> predicates = domainUnits.stream()
                    .map(DomainUnit::initPredicateForDouble)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (predicates.isEmpty()) { return null; }
            if (predicates.size() == 1) { return predicates.get(0); }
            return value -> {
                for (DoublePredicate predicate : predicates) {
                    if (!predicate.test(value)) {
                        return false;
                    }
                }
                return true;
            };
        }

        @Override
        public BytePredicate initPredicateForByte() {
            List<BytePredicate> predicates = domainUnits.stream()
                    .map(DomainUnit::initPredicateForByte)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (predicates.isEmpty()) { return null; }
            if (predicates.size() == 1) { return predicates.get(0); }
            return value -> {
                for (BytePredicate predicate : predicates) {
                    if (!predicate.test(value)) {
                        return false;
                    }
                }
                return true;
            };
        }

        @Override
        public ShortPredicate initPredicateForShort() {
            List<ShortPredicate> predicates = domainUnits.stream()
                    .map(DomainUnit::initPredicateForShort)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (predicates.isEmpty()) { return null; }
            if (predicates.size() == 1) { return predicates.get(0); }
            return value -> {
                for (ShortPredicate predicate : predicates) {
                    if (!predicate.test(value)) {
                        return false;
                    }
                }
                return true;
            };
        }

        @Override
        public FloatPredicate initPredicateForFloat() {
            List<FloatPredicate> predicates = domainUnits.stream()
                    .map(DomainUnit::initPredicateForFloat)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (predicates.isEmpty()) { return null; }
            if (predicates.size() == 1) { return predicates.get(0); }
            return value -> {
                for (FloatPredicate predicate : predicates) {
                    if (!predicate.test(value)) {
                        return false;
                    }
                }
                return true;
            };
        }

    }

    /** Number set like { 1, 3, 7 }. */
    private static class NumberSet implements DomainUnit {

        /** Numeric texts. */
        private final String[] numberTextSet;

        /**
         * Construct a {@code NumberSet}.
         *
         * @param numberTextSet number text set
         */
        public NumberSet(String[] numberTextSet) {
            this.numberTextSet = numberTextSet;
        }

        @Override
        public Predicate<CharSequence> initPredicateForNumericText() {
            Set<BigDecimal> values = Arrays.stream(numberTextSet)
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
        public <T extends Number & Comparable<T>> Predicate<T> initPredicateForComparableNumber(
                Class<T> type) {
            List<T> refs = Arrays.stream(numberTextSet)
                    .map(text -> NumericTextParser.parse(type, text))
                    .sorted()
                    .collect(Collectors.toCollection(LinkedList::new));
            return value -> (Collections.binarySearch(refs, value) >= 0);
        }

        @Override
        public IntPredicate initPredicateForInt() {
            int[] refs = Arrays.stream(numberTextSet)
                    .mapToInt(Integer::parseInt).sorted().distinct().toArray();
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

        @Override
        public LongPredicate initPredicateForLong() {
            long[] refs = Arrays.stream(numberTextSet)
                    .mapToLong(Long::parseLong).sorted().distinct().toArray();
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

        @Override
        public DoublePredicate initPredicateForDouble() {
            double[] refs = Arrays.stream(numberTextSet)
                    .mapToDouble(Double::parseDouble).sorted().distinct().toArray();
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

        @Override
        public BytePredicate initPredicateForByte() {
            byte[] refs = new byte[numberTextSet.length];
            for (int i = 0; i < numberTextSet.length; i++) {
                refs[i] = Byte.parseByte(numberTextSet[i]);
            }
            Arrays.sort(refs);
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

        @Override
        public ShortPredicate initPredicateForShort() {
            short[] refs = new short[numberTextSet.length];
            for (int i = 0; i < numberTextSet.length; i++) {
                refs[i] = Short.parseShort(numberTextSet[i]);
            }
            Arrays.sort(refs);
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

        @Override
        public FloatPredicate initPredicateForFloat() {
            float[] refs = new float[numberTextSet.length];
            for (int i = 0; i < numberTextSet.length; i++) {
                refs[i] = Float.parseFloat(numberTextSet[i]);
            }
            Arrays.sort(refs);
            return value -> (Arrays.binarySearch(refs, value) >= 0);
        }

    }

    /** Range unit like [3, ~). */
    private static class RangeUnit implements DomainUnit {

        /** Infinity. */
        private static final String INFINITY = "~";

        /** Min text. */
        private final String minText;
        /** Max text. */
        private final String maxText;
        /** Is negative infinity. */
        private final boolean isNegativeInfinity;
        /** Is positive infinity. */
        private final boolean isPositiveInfinity;
        /** Include min. */
        private final boolean includeMin;
        /** Include max. */
        private final boolean includeMax;

        /**
         * Construct a {@code RangeUnit}.
         *
         * @param rangeUnitText range unit text
         */
        public RangeUnit(String rangeUnitText) {
            rangeUnitText = rangeUnitText.replaceAll("\\s", "");
            Matcher matcher = Pattern.compile("^([\\[(])([^,]+),(.+)([])])$").matcher(rangeUnitText);
            if (!matcher.find()) { throw new IllegalArgumentException("Illegal range unit text: " + rangeUnitText); }
            minText = matcher.group(2);
            maxText = matcher.group(3);
            isNegativeInfinity = INFINITY.equals(minText);
            isPositiveInfinity = INFINITY.equals(maxText);
            includeMin = "[".equals(matcher.group(1));
            includeMax = "]".equals(matcher.group(4));
            if (!isNegativeInfinity) { new BigDecimal(minText); }
            if (!isPositiveInfinity) { new BigDecimal(maxText); }
        }

        @Override
        public Predicate<CharSequence> initPredicateForNumericText() {
            if (isNegativeInfinity && isPositiveInfinity) { return null; }
            BigDecimal min = isNegativeInfinity ? null : new BigDecimal(minText);
            BigDecimal max = isPositiveInfinity ? null : new BigDecimal(maxText);
            boolean includeMin = this.includeMin;
            boolean includeMax = this.includeMax;
            if (isNegativeInfinity) {
                return value -> {
                    BigDecimal bigDecimal = parseNumericTextToBigDecimal(value);
                    int right = bigDecimal.compareTo(max);
                    return (right < 0) || (includeMax && right == 0);
                };
            } else if (isPositiveInfinity) {
                return value -> {
                    BigDecimal bigDecimal = parseNumericTextToBigDecimal(value);
                    int left = bigDecimal.compareTo(min);
                    return (left > 0) || (includeMin && left == 0);
                };
            } else {
                return value -> {
                    BigDecimal bigDecimalValue = parseNumericTextToBigDecimal(value);
                    int left = bigDecimalValue.compareTo(min);
                    int right = bigDecimalValue.compareTo(max);
                    return ((left > 0) || (includeMin && left == 0)) && (right < 0) || (includeMax && right == 0);
                };
            }
        }

        @Override
        public <T extends Number & Comparable<T>> Predicate<T> initPredicateForComparableNumber(Class<T> type) {
            if (isNegativeInfinity && isPositiveInfinity) { return null; }
            T min = isNegativeInfinity ? null : NumericTextParser.parse(type, minText);
            T max = isPositiveInfinity ? null : NumericTextParser.parse(type, maxText);
            boolean includeMin = this.includeMin;
            boolean includeMax = this.includeMax;
            if (isNegativeInfinity) {
                return value -> {
                    int right = value.compareTo(max);
                    return (right < 0) || (includeMax && right == 0);
                };
            } else if (isPositiveInfinity) {
                return value -> {
                    int left = value.compareTo(min);
                    return (left > 0) || (includeMin && left == 0);
                };
            } else {
                return value -> {
                    int left = value.compareTo(min);
                    int right = value.compareTo(max);
                    return ((left > 0) || (includeMin && left == 0)) && (right < 0) || (includeMax && right == 0);
                };
            }
        }

        @Override
        public IntPredicate initPredicateForInt() {
            if (isNegativeInfinity && isPositiveInfinity) { return null; }
            int min = isNegativeInfinity ? Integer.MIN_VALUE : Integer.parseInt(minText);
            int max = isPositiveInfinity ? Integer.MAX_VALUE : Integer.parseInt(maxText);
            boolean includeMin = this.includeMin;
            boolean includeMax = this.includeMax;
            if (isNegativeInfinity) {
                return value -> (value < max) || (includeMax && value == max);
            } else if (isPositiveInfinity) {
                return value -> (value > min) || (includeMin && value == min);
            } else {
                return value -> ((value < max) || (includeMax && value == max))
                        && ((value > min) || (includeMin && value == min));
            }
        }

        @Override
        public LongPredicate initPredicateForLong() {
            if (isNegativeInfinity && isPositiveInfinity) { return null; }
            long min = isNegativeInfinity ? Long.MIN_VALUE : Long.parseLong(minText);
            long max = isPositiveInfinity ? Long.MAX_VALUE : Long.parseLong(maxText);
            boolean includeMin = this.includeMin;
            boolean includeMax = this.includeMax;
            if (isNegativeInfinity) {
                return value -> (value < max) || (includeMax && value == max);
            } else if (isPositiveInfinity) {
                return value -> (value > min) || (includeMin && value == min);
            } else {
                return value -> ((value < max) || (includeMax && value == max))
                        && ((value > min) || (includeMin && value == min));
            }
        }

        @Override
        public DoublePredicate initPredicateForDouble() {
            if (isNegativeInfinity && isPositiveInfinity) { return null; }
            double min = isNegativeInfinity ? Double.MIN_VALUE : Double.parseDouble(minText);
            double max = isPositiveInfinity ? Double.MAX_VALUE : Double.parseDouble(maxText);
            boolean includeMin = this.includeMin;
            boolean includeMax = this.includeMax;
            if (isNegativeInfinity) {
                return value -> (value < max) || (includeMax && value == max);
            } else if (isPositiveInfinity) {
                return value -> (value > min) || (includeMin && value == min);
            } else {
                return value -> ((value < max) || (includeMax && value == max))
                        && ((value > min) || (includeMin && value == min));
            }
        }

        @Override
        public BytePredicate initPredicateForByte() {
            if (isNegativeInfinity && isPositiveInfinity) { return null; }
            byte min = isNegativeInfinity ? Byte.MIN_VALUE : Byte.parseByte(minText);
            byte max = isPositiveInfinity ? Byte.MAX_VALUE : Byte.parseByte(maxText);
            boolean includeMin = this.includeMin;
            boolean includeMax = this.includeMax;
            if (isNegativeInfinity) {
                return value -> (value < max) || (includeMax && value == max);
            } else if (isPositiveInfinity) {
                return value -> (value > min) || (includeMin && value == min);
            } else {
                return value -> ((value < max) || (includeMax && value == max))
                        && ((value > min) || (includeMin && value == min));
            }
        }

        @Override
        public ShortPredicate initPredicateForShort() {
            if (isNegativeInfinity && isPositiveInfinity) { return null; }
            short min = isNegativeInfinity ? Short.MIN_VALUE : Short.parseShort(minText);
            short max = isPositiveInfinity ? Short.MAX_VALUE : Short.parseShort(maxText);
            boolean includeMin = this.includeMin;
            boolean includeMax = this.includeMax;
            if (isNegativeInfinity) {
                return value -> (value < max) || (includeMax && value == max);
            } else if (isPositiveInfinity) {
                return value -> (value > min) || (includeMin && value == min);
            } else {
                return value -> ((value < max) || (includeMax && value == max))
                        && ((value > min) || (includeMin && value == min));
            }
        }

        @Override
        public FloatPredicate initPredicateForFloat() {
            if (isNegativeInfinity && isPositiveInfinity) { return null; }
            float min = isNegativeInfinity ? Float.MIN_VALUE : Float.parseFloat(minText);
            float max = isPositiveInfinity ? Float.MAX_VALUE : Float.parseFloat(maxText);
            boolean includeMin = this.includeMin;
            boolean includeMax = this.includeMax;
            if (isNegativeInfinity) {
                return value -> (value < max) || (includeMax && value == max);
            } else if (isPositiveInfinity) {
                return value -> (value > min) || (includeMin && value == min);
            } else {
                return value -> ((value < max) || (includeMax && value == max))
                        && ((value > min) || (includeMin && value == min));
            }
        }

    }

    /** Domain unit. */
    private interface DomainUnit {

        /**
         * Initialize {@code Predicate} for numeric text.
         *
         * @return {@code Predicate} for numeric text
         */
        Predicate<CharSequence> initPredicateForNumericText();

        /**
         * Initialize {@code Predicate} for {@code Comparable & Number} value.
         *
         * @param type type of {@code Comparable & Number}
         * @param <T> type of {@code Comparable & Number}
         * @return {@code Predicate} for {@code Comparable & Number} value
         */
        <T extends Number & Comparable<T>> Predicate<T> initPredicateForComparableNumber(Class<T> type);

        /**
         * Initialize {@code Predicate} for {@code int} value.
         *
         * @return {@code Predicate} for {@code int} value
         */
        IntPredicate initPredicateForInt();

        /**
         * Initialize {@code Predicate} for {@code long} value.
         *
         * @return {@code Predicate} for {@code long} value
         */
        LongPredicate initPredicateForLong();

        /**
         * Initialize {@code Predicate} for {@code double} value.
         *
         * @return {@code Predicate} for {@code double} value
         */
        DoublePredicate initPredicateForDouble();

        /**
         * Initialize {@code Predicate} for {@code byte} value.
         *
         * @return {@code Predicate} for {@code byte} value
         */
        BytePredicate initPredicateForByte();

        /**
         * Initialize {@code Predicate} for {@code short} value.
         *
         * @return {@code Predicate} for {@code short} value
         */
        ShortPredicate initPredicateForShort();

        /**
         * Initialize {@code Predicate} for {@code float} value.
         *
         * @return {@code Predicate} for {@code float} value
         */
        FloatPredicate initPredicateForFloat();

    }

}
