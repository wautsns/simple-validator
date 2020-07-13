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

import com.github.wautsns.simplevalidator.constraint.number.domain.DomainUtils;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.factory.special.CharSequenceCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.IntPredicate;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VChineseIdCardCharSequenceCriterionFactory extends CharSequenceCriterionFactory<VChineseIdCard> {

    /** {@code VChineseIdCardCharSequenceCriterionFactory} instance */
    public static final VChineseIdCardCharSequenceCriterionFactory INSTANCE = new VChineseIdCardCharSequenceCriterionFactory();

    @Override
    public void process(ConstrainedNode node, VChineseIdCard constraint, TCriteria<CharSequence> wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @return criterion
     */
    protected static TCriterion<CharSequence> produce(VChineseIdCard constraint) {
        Set<VChineseIdCard.Generation> generations = InternalUtils.simplifyGenerations(constraint.generations());
        EnumMap<VChineseIdCard.Generation, TCriterion<CharSequence>> criteria = new EnumMap<>(
                VChineseIdCard.Generation.class);
        if (generations.contains(VChineseIdCard.Generation.SECOND)) {
            criteria.put(VChineseIdCard.Generation.SECOND, initForSecondGeneration(constraint));
        }
        if (generations.contains(VChineseIdCard.Generation.FIRST)) {
            criteria.put(VChineseIdCard.Generation.FIRST, initForFirstGeneration(constraint));
        }
        if (criteria.size() == 1) { return criteria.values().iterator().next(); }
        return id -> {
            ValidationFailure validationFailure = null;
            for (VChineseIdCard.Generation generation : InternalUtils.guessGenerationRoughly(id)) {
                TCriterion<CharSequence> criterion = criteria.get(generation);
                if (criterion == null) { continue; }
                validationFailure = criterion.test(id);
                if (validationFailure == null) { return null; }
            }
            return (validationFailure == null) ? fail(id) : validationFailure;
        };
    }

    /**
     * Initialize a criterion for the second generation id card.
     *
     * @param constraint constraint
     * @return criterion for second generation id card
     */
    private static TCriterion<CharSequence> initForSecondGeneration(VChineseIdCard constraint) {
        Map<Integer, String> cityCodeNameMap = InternalUtils.toCityNameMap(constraint.cities());
        IntPredicate agePredicate = DomainUtils.init(constraint.ages()).forInt();
        Set<VChineseIdCard.Gender> genderSet = InternalUtils.simplifyGenders(constraint.genders());
        return id -> {
            boolean correct;
            if (id.length() != 18) { return fail(id); }
            if (!isDigits(id, 0, 17)) { return fail(id); }
            correct = InternalUtils.checkSecondGenerationCheckCodeChar(id);
            if (!correct) { return fail(id); }
            String cityName = cityCodeNameMap.get(toNaturalNumber(id, 0, 2));
            if (cityName == null) { return fail(id); }
            LocalDate birthday = InternalUtils.getBirthday(
                    toNaturalNumber(id, 6, 4),
                    toNaturalNumber(id, 10, 2),
                    toNaturalNumber(id, 12, 2));
            if (birthday == null) { return fail(id); }
            int age = InternalUtils.getAge(birthday);
            if (agePredicate != null && !agePredicate.test(age)) { return fail(id); }
            VChineseIdCard.Gender gender = InternalUtils.getGender(toNaturalNumber(id, 14, 3));
            if (!genderSet.contains(gender)) { return fail(id); }
            correct = InternalUtils.checkSecondGenerationCheckCode(id);
            return correct ? null : fail(id);
        };
    }

    /**
     * Initialize a criterion for the first generation id card.
     *
     * @param constraint constraint
     * @return criterion for first generation id card
     */
    private static TCriterion<CharSequence> initForFirstGeneration(VChineseIdCard constraint) {
        Map<Integer, String> cityNameMap = InternalUtils.toCityNameMap(constraint.cities());
        IntPredicate agePredicate = DomainUtils.init(constraint.ages()).forInt();
        Set<VChineseIdCard.Gender> genders = InternalUtils.simplifyGenders(constraint.genders());
        return id -> {
            if (id.length() != 15) { return fail(id); }
            if (!isDigits(id, 0, 15)) { return fail(id); }
            String cityName = cityNameMap.get(toNaturalNumber(id, 0, 2));
            if (cityName == null) { return fail(id); }
            LocalDate birthday = InternalUtils.getBirthday(
                    1900 + toNaturalNumber(id, 6, 2),
                    toNaturalNumber(id, 8, 2),
                    toNaturalNumber(id, 10, 2));
            if (birthday == null) { return fail(id); }
            int age = InternalUtils.getAge(birthday);
            if (agePredicate != null && !agePredicate.test(age)) { return fail(id); }
            VChineseIdCard.Gender gender = InternalUtils.getGender(toNaturalNumber(id, 12, 3));
            if (!genders.contains(gender)) { return fail(id); }
            return null;
        };
    }

    /**
     * Return validation failure with the specified value.
     *
     * @param value value
     * @return validation failure with the specified value
     */
    private static ValidationFailure fail(CharSequence value) {
        return new ValidationFailure(value);
    }

    /**
     * Return whether the chars of subtext are digits.
     *
     * @param text text
     * @param startIndex start index
     * @param length length
     * @return {@code true} the chars of subtext are digits, otherwise {@code false}
     */
    private static boolean isDigits(CharSequence text, int startIndex, int length) {
        for (int i = 0, l = i + length; i < l; i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convert subtext to natural number.
     *
     * @param text text
     * @param startIndex start index
     * @param length length
     * @return natural number
     */
    private static int toNaturalNumber(CharSequence text, int startIndex, int length) {
        int rst = 0;
        for (int i = startIndex, l = i + length; i < l; i++) {
            rst = rst * 10 + (text.charAt(i) - '0');
        }
        return rst;
    }

}
