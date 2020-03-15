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
import com.github.wautsns.simplevalidator.model.criterion.factory.special.AbstractCharSequenceCriterionFactory;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.IntPredicate;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VChineseIdCardCharSequenceCriterionFactory
        extends AbstractCharSequenceCriterionFactory<VChineseIdCard> {

    @Override
    public void process(ConstrainedNode node, VChineseIdCard constraint, TCriteria<CharSequence> wip) {
        wip.add(produce(constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected static TCriterion<CharSequence> produce(VChineseIdCard constraint) {
        Set<VChineseIdCard.Generation> generations = ChineseIdCardUtils.simplify(constraint.generations());
        EnumMap<VChineseIdCard.Generation, TCriterion<CharSequence>> criteria = new EnumMap<>(
                VChineseIdCard.Generation.class);
        if (generations.contains(VChineseIdCard.Generation.SECOND)) {
            criteria.put(VChineseIdCard.Generation.SECOND, initGenerationSecond(constraint));
        }
        if (generations.contains(VChineseIdCard.Generation.FIRST)) {
            criteria.put(VChineseIdCard.Generation.FIRST, initGenerationFirst(constraint));
        }
        if (criteria.size() == 1) {
            return criteria.values().iterator().next();
        }
        return id -> {
            VChineseIdCard.Generation generation = ChineseIdCardUtils.getGeneration(id);
            TCriterion<CharSequence> criterion = criteria.get(generation);
            return (criterion == null) ? wrong(id) : criterion.test(id);
        };
    }

    private static TCriterion<CharSequence> initGenerationSecond(VChineseIdCard constraint) {
        Map<Integer, String> cityCodeNameMap = ChineseIdCardUtils.toCityCodeNameMap(constraint.cities());
        IntPredicate agePredicate = DomainUtils.init(constraint.ages()).forInt();
        Set<VChineseIdCard.Gender> genderSet = ChineseIdCardUtils.simplify(constraint.genders());
        return id -> {
            boolean correct;
            VChineseIdCard.Generation generation = ChineseIdCardUtils.getGeneration(id);
            if (generation != VChineseIdCard.Generation.SECOND) { return new ValidationFailure(id); }
            if (!isDigits(id, 17)) { return new ValidationFailure(id); }
            correct = ChineseIdCardUtils.checkSecondGenerationCheckCodeChar(id);
            if (!correct) { return wrong(id); }
            String cityName = cityCodeNameMap.get(toNaturalNumber(id, 0, 2));
            if (cityName == null) { return wrong(id); }
            LocalDate birthday = ChineseIdCardUtils.getBirthday(
                    toNaturalNumber(id, 6, 4),
                    toNaturalNumber(id, 10, 2),
                    toNaturalNumber(id, 12, 2));
            if (birthday == null) { return wrong(id); }
            int age = ChineseIdCardUtils.getAge(birthday);
            if (agePredicate != null && !agePredicate.test(age)) { return wrong(id); }
            VChineseIdCard.Gender gender = ChineseIdCardUtils.getGender(toNaturalNumber(id, 14, 3));
            if (!genderSet.contains(gender)) { return wrong(id); }
            correct = ChineseIdCardUtils.checkSecondGenerationCheckCode(id);
            return correct ? null : wrong(id);
        };
    }

    private static TCriterion<CharSequence> initGenerationFirst(VChineseIdCard constraint) {
        Map<Integer, String> cityCodeNameMap = ChineseIdCardUtils
                .toCityCodeNameMap(constraint.cities());
        IntPredicate agePredicate = DomainUtils.init(constraint.ages()).forInt();
        Set<VChineseIdCard.Gender> genderSet = ChineseIdCardUtils.simplify(constraint.genders());
        return id -> {
            VChineseIdCard.Generation generation = ChineseIdCardUtils.getGeneration(id);
            if (generation != VChineseIdCard.Generation.FIRST) { return wrong(id); }
            if (!isDigits(id, 15)) { return wrong(id); }
            String cityName = cityCodeNameMap.get(toNaturalNumber(id, 0, 2));
            if (cityName == null) { return wrong(id); }
            LocalDate birthday = ChineseIdCardUtils.getBirthday(
                    1900 + toNaturalNumber(id, 6, 2),
                    toNaturalNumber(id, 8, 2),
                    toNaturalNumber(id, 10, 2));
            if (birthday == null) { return wrong(id); }
            int age = ChineseIdCardUtils.getAge(birthday);
            if (agePredicate != null && !agePredicate.test(age)) { return wrong(id); }
            VChineseIdCard.Gender gender = ChineseIdCardUtils.getGender(toNaturalNumber(id, 12, 3));
            if (!genderSet.contains(gender)) { return wrong(id); }
            return null;
        };
    }

    private static ValidationFailure wrong(CharSequence id) {
        return new ValidationFailure(id);
    }

    private static boolean isDigits(CharSequence text, int length) {
        for (int i = 0, l = i + length; i < l; i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static int toNaturalNumber(CharSequence text, int startIndex, int length) {
        int rst = 0;
        for (int i = startIndex, l = i + length; i < l; i++) {
            rst = rst * 10 + (text.charAt(i) - '0');
        }
        return rst;
    }

}
