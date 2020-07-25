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
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForCharSequence;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.IntPredicate;

/**
 * VChineseIdCard criterion factory for {@code CharSequence} value.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VChineseIdCardCriterionFactoryForCharSequence extends CriterionFactoryForCharSequence<VChineseIdCard> {

    /** {@code VChineseIdCardCriterionFactoryForCharSequence} instance. */
    public static final VChineseIdCardCriterionFactoryForCharSequence INSTANCE = new VChineseIdCardCriterionFactoryForCharSequence();

    @Override
    public void process(ConstrainedNode node, VChineseIdCard constraint, CriteriaForNonPrimitive<CharSequence> wip) {
        wip.add(produce(constraint));
    }

    // #################### criterion ###################################################

    /**
     * Produce criterion.
     *
     * @param constraint constraint
     * @return criterion
     */
    protected static CriterionForNonPrimitive<CharSequence> produce(VChineseIdCard constraint) {
        Set<VChineseIdCard.Generation> generations = InternalUtils.simplifyGenerations(constraint.generations());
        EnumMap<VChineseIdCard.Generation, CriterionForNonPrimitive<CharSequence>> criteria = new EnumMap<>(
                VChineseIdCard.Generation.class);
        if (generations.contains(VChineseIdCard.Generation.SECOND)) {
            criteria.put(VChineseIdCard.Generation.SECOND, initForSecondGeneration(constraint));
        }
        if (generations.contains(VChineseIdCard.Generation.FIRST)) {
            criteria.put(VChineseIdCard.Generation.FIRST, initForFirstGeneration(constraint));
        }
        if (criteria.size() == 1) { return criteria.values().iterator().next(); }
        return new CriterionForNonPrimitive<CharSequence>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(CharSequence value) {
                ValidationFailure validationFailure = null;
                for (VChineseIdCard.Generation generation : InternalUtils.guessGenerationRoughly(value)) {
                    CriterionForNonPrimitive<CharSequence> criterion = criteria.get(generation);
                    if (criterion == null) { continue; }
                    validationFailure = criterion.test(value);
                    if (validationFailure == null) { return null; }
                }
                return (validationFailure == null) ? fail(value) : validationFailure;
            }
        };
    }

    /**
     * Initialize a criterion for the second generation id card.
     *
     * @param constraint constraint
     * @return criterion for second generation id card
     */
    private static CriterionForNonPrimitive<CharSequence> initForSecondGeneration(VChineseIdCard constraint) {
        Map<Integer, String> cityCodeNameMap = InternalUtils.toCityNameMap(constraint.cities());
        IntPredicate agePredicate = DomainUtils.init(constraint.ages()).initPredicateForInt();
        Set<VChineseIdCard.Gender> genderSet = InternalUtils.simplifyGenders(constraint.genders());
        return new CriterionForNonPrimitive<CharSequence>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(CharSequence value) {
                boolean correct;
                if (value.length() != 18) { return fail(value); }
                if (!isDigits(value, 0, 17)) { return fail(value); }
                correct = InternalUtils.checkSecondGenerationCheckCodeChar(value);
                if (!correct) { return fail(value); }
                String cityName = cityCodeNameMap.get(toNaturalNumber(value, 0, 2));
                if (cityName == null) { return fail(value); }
                LocalDate birthday = InternalUtils.getBirthday(
                        toNaturalNumber(value, 6, 4),
                        toNaturalNumber(value, 10, 2),
                        toNaturalNumber(value, 12, 2));
                if (birthday == null) { return fail(value); }
                int age = InternalUtils.getAge(birthday);
                if (agePredicate != null && !agePredicate.test(age)) { return fail(value); }
                VChineseIdCard.Gender gender = InternalUtils.getGender(toNaturalNumber(value, 14, 3));
                if (!genderSet.contains(gender)) { return fail(value); }
                correct = InternalUtils.checkSecondGenerationCheckCode(value);
                return correct ? null : fail(value);
            }
        };
    }

    /**
     * Initialize a criterion for the first generation id card.
     *
     * @param constraint constraint
     * @return criterion for first generation id card
     */
    private static CriterionForNonPrimitive<CharSequence> initForFirstGeneration(VChineseIdCard constraint) {
        Map<Integer, String> cityNameMap = InternalUtils.toCityNameMap(constraint.cities());
        IntPredicate agePredicate = DomainUtils.init(constraint.ages()).initPredicateForInt();
        Set<VChineseIdCard.Gender> genders = InternalUtils.simplifyGenders(constraint.genders());
        return new CriterionForNonPrimitive<CharSequence>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(CharSequence value) {
                if (value.length() != 15) { return fail(value); }
                if (!isDigits(value, 0, 15)) { return fail(value); }
                String cityName = cityNameMap.get(toNaturalNumber(value, 0, 2));
                if (cityName == null) { return fail(value); }
                LocalDate birthday = InternalUtils.getBirthday(
                        1900 + toNaturalNumber(value, 6, 2),
                        toNaturalNumber(value, 8, 2),
                        toNaturalNumber(value, 10, 2));
                if (birthday == null) { return fail(value); }
                int age = InternalUtils.getAge(birthday);
                if (agePredicate != null && !agePredicate.test(age)) { return fail(value); }
                VChineseIdCard.Gender gender = InternalUtils.getGender(toNaturalNumber(value, 12, 3));
                if (!genders.contains(gender)) { return fail(value); }
                return null;
            }
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
