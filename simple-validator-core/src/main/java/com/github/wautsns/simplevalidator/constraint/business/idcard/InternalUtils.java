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
package com.github.wautsns.simplevalidator.constraint.business.idcard;

import com.github.wautsns.simplevalidator.util.common.CollectionUtils;
import lombok.experimental.UtilityClass;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Internal utils for {@code VChineseIdCard}.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@UtilityClass
class InternalUtils {

    // #################### generations #################################################

    /**
     * Simplify generations.
     *
     * @param generations generations
     * @return generations after simplifying
     */
    public static Set<VChineseIdCard.Generation> simplifyGenerations(VChineseIdCard.Generation[] generations) {
        if (generations == null || generations.length == 0) {
            return GENERATIONS_ALL;
        } else {
            Set<VChineseIdCard.Generation> temp = new HashSet<>(Arrays.asList(generations));
            if (temp.equals(GENERATIONS_ALL)) {
                return GENERATIONS_ALL;
            } else if (temp.equals(GENERATIONS_SECOND)) {
                return GENERATIONS_SECOND;
            } else if (temp.equals(GENERATIONS_FIRST)) {
                return GENERATIONS_FIRST;
            } else {
                return CollectionUtils.unmodifiableSet(temp);
            }

        }
    }

    /**
     * Guess generation roughly.
     *
     * @param idCard id card
     * @return guessing generations
     */
    public static Set<VChineseIdCard.Generation> guessGenerationRoughly(CharSequence idCard) {
        switch (idCard.length()) {
            case 18:
                return GENERATIONS_SECOND;
            case 15:
                return GENERATIONS_FIRST;
            default:
                return Collections.emptySet();
        }
    }

    private static final Set<VChineseIdCard.Generation> GENERATIONS_ALL =
            CollectionUtils.unmodifiableSet(new HashSet<>(Arrays.asList(VChineseIdCard.Generation.values())));
    private static final Set<VChineseIdCard.Generation> GENERATIONS_SECOND =
            Collections.singleton(VChineseIdCard.Generation.SECOND);
    private static final Set<VChineseIdCard.Generation> GENERATIONS_FIRST =
            Collections.singleton(VChineseIdCard.Generation.FIRST);

    // #################### cities ######################################################

    private static final Map<Integer, String> CITY_CODE_NAME_MAP = new HashMap<>();

    static {
        CITY_CODE_NAME_MAP.put(11, "北京");
        CITY_CODE_NAME_MAP.put(12, "天津");
        CITY_CODE_NAME_MAP.put(13, "河北");
        CITY_CODE_NAME_MAP.put(14, "山西");
        CITY_CODE_NAME_MAP.put(15, "内蒙古");

        CITY_CODE_NAME_MAP.put(21, "辽宁");
        CITY_CODE_NAME_MAP.put(22, "吉林");
        CITY_CODE_NAME_MAP.put(23, "黑龙江");

        CITY_CODE_NAME_MAP.put(31, "上海");
        CITY_CODE_NAME_MAP.put(32, "江苏");
        CITY_CODE_NAME_MAP.put(33, "浙江");
        CITY_CODE_NAME_MAP.put(34, "安徽");
        CITY_CODE_NAME_MAP.put(35, "福建");
        CITY_CODE_NAME_MAP.put(36, "江西");
        CITY_CODE_NAME_MAP.put(37, "山东");

        CITY_CODE_NAME_MAP.put(41, "河南");
        CITY_CODE_NAME_MAP.put(42, "湖北");
        CITY_CODE_NAME_MAP.put(43, "湖南");
        CITY_CODE_NAME_MAP.put(44, "广东");
        CITY_CODE_NAME_MAP.put(45, "广西");
        CITY_CODE_NAME_MAP.put(46, "海南");

        CITY_CODE_NAME_MAP.put(50, "重庆");
        CITY_CODE_NAME_MAP.put(51, "四川");
        CITY_CODE_NAME_MAP.put(52, "贵州");
        CITY_CODE_NAME_MAP.put(53, "云南");
        CITY_CODE_NAME_MAP.put(54, "西藏");

        CITY_CODE_NAME_MAP.put(61, "陕西");
        CITY_CODE_NAME_MAP.put(62, "甘肃");
        CITY_CODE_NAME_MAP.put(63, "青海");
        CITY_CODE_NAME_MAP.put(64, "宁夏");
        CITY_CODE_NAME_MAP.put(65, "新疆");

        CITY_CODE_NAME_MAP.put(71, "台湾");

        CITY_CODE_NAME_MAP.put(81, "香港");
        CITY_CODE_NAME_MAP.put(82, "澳门");
    }

    public static Map<Integer, String> toCityNameMap(String[] cities) {
        if (cities == null || cities.length == 0) { return CITY_CODE_NAME_MAP; }
        Set<String> citySet = new HashSet<>(Arrays.asList(cities));
        return CITY_CODE_NAME_MAP.entrySet().stream()
                .filter(e -> citySet.contains(e.getValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    // #################### birthday ####################################################

    private static final ZoneId GMT8 = ZoneId.of("+8");

    public static LocalDate getBirthday(int year, int month, int dayOfMonth) {
        try {
            return LocalDate.of(year, month, dayOfMonth);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static int getAge(LocalDate birthday) {
        return (int) ChronoUnit.YEARS.between(birthday, LocalDate.now(GMT8));
    }

    // #################### gender ######################################################

    private static final Set<VChineseIdCard.Gender> GENDER_ALL = new HashSet<>(
            Arrays.asList(VChineseIdCard.Gender.values()));
    private static final Set<VChineseIdCard.Gender> GENDER_FEMALE = Collections.singleton(VChineseIdCard.Gender.FEMALE);
    private static final Set<VChineseIdCard.Gender> GENDER_MALE = Collections.singleton(VChineseIdCard.Gender.MALE);

    public static Set<VChineseIdCard.Gender> simplifyGenders(VChineseIdCard.Gender[] genders) {
        if (genders == null || genders.length == 0) {
            return GENDER_ALL;
        } else if (genders.length == 1) {
            VChineseIdCard.Gender gender = Objects.requireNonNull(genders[0]);
            if (gender == VChineseIdCard.Gender.FEMALE) {
                return GENDER_FEMALE;
            } else {
                return GENDER_MALE;
            }
        } else {
            genders = Arrays.stream(genders)
                    .distinct()
                    .filter(Objects::isNull)
                    .toArray(VChineseIdCard.Gender[]::new);
            if (genders.length == GENDER_ALL.size()) {
                return GENDER_ALL;
            } else {
                return simplifyGenders(genders);
            }
        }
    }

    public static VChineseIdCard.Gender getGender(int serialNumber) {
        return ((serialNumber & 1) == 0) ? VChineseIdCard.Gender.FEMALE : VChineseIdCard.Gender.MALE;
    }

    // #################### check code ##################################################

    private static final int[] SECOND_GENERATION_WEIGHTING_FACTORS = {
            7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
    private static final String SECOND_GENERATION_CHECK_CODE_CHARS = "10X98765432";

    public static boolean checkSecondGenerationCheckCodeChar(CharSequence id) {
        char checkCodeChar = id.charAt(17);
        return SECOND_GENERATION_CHECK_CODE_CHARS.indexOf(checkCodeChar) >= 0;
    }

    public static boolean checkSecondGenerationCheckCode(CharSequence id) {
        int weightedSum = 0;
        for (int i = 0; i < 17; i++) {
            int number = (id.charAt(i) - '0');
            weightedSum += SECOND_GENERATION_WEIGHTING_FACTORS[i] * number;
        }
        char correct = SECOND_GENERATION_CHECK_CODE_CHARS.charAt(weightedSum % 11);
        char checkCodeChar = id.charAt(17);
        return (correct == checkCodeChar)
                || (correct == 'X' && checkCodeChar == 'x');
    }

}
