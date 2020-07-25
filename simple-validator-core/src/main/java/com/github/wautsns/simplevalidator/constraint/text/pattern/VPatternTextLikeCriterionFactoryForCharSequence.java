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
package com.github.wautsns.simplevalidator.constraint.text.pattern;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForCharSequence;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VPatternTextLikeCriterionFactoryForCharSequence extends CriterionFactoryForCharSequence<VPattern> {

    public static final VPatternTextLikeCriterionFactoryForCharSequence INSTANCE = new VPatternTextLikeCriterionFactoryForCharSequence();

    @Override
    public void process(ConstrainedNode node, VPattern constraint, CriteriaForNonPrimitive<CharSequence> wip) {
        wip.add(produce(constraint));
    }
    // #################### criterion ###################################################

    protected static CriterionForNonPrimitive<CharSequence> produce(VPattern constraint) {
        Pattern pattern = Pattern.compile(constraint.regex());
        return new CriterionForNonPrimitive<CharSequence>() {
            @Override
            protected ValidationFailure testWithoutEnhancingFailure(CharSequence value) {
                return pattern.matcher(value).matches() ? null : new ValidationFailure(value);
            }
        };
    }

}
