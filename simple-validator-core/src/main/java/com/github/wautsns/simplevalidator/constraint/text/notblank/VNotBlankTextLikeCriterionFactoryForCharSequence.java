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
package com.github.wautsns.simplevalidator.constraint.text.notblank;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.factory.special.CriterionFactoryForCharSequence;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VNotBlankTextLikeCriterionFactoryForCharSequence extends CriterionFactoryForCharSequence<VNotBlank> {

    public static final VNotBlankTextLikeCriterionFactoryForCharSequence INSTANCE = new VNotBlankTextLikeCriterionFactoryForCharSequence();

    @Override
    public void process(ConstrainedNode node, VNotBlank constraint, CriteriaForNonPrimitive<CharSequence> wip) {
        wip.add(CRITERION);
    }

    // #################### criterion ###################################################

    private static final CriterionForNonPrimitive<CharSequence> CRITERION = new CriterionForNonPrimitive<CharSequence>() {
        @Override
        protected ValidationFailure testWithoutEnhancingFailure(CharSequence value) {
            for (int i = 0; i < value.length(); i++) {
                if (!Character.isWhitespace(value.charAt(i))) {
                    return null;
                }
            }
            return new ValidationFailure(value);
        }
    };

}
