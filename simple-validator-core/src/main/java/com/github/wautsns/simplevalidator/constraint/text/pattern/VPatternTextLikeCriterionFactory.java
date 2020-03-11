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

import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.text.AbstractTextLikeCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.text.TextLikeUtility;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriterion;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

import java.util.regex.Pattern;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VPatternTextLikeCriterionFactory extends AbstractTextLikeCriterionFactory<VPattern> {

    @Override
    protected <T> void process(
            TextLikeUtility<T> utility,
            ConstrainedNode element, VPattern constraint, TCriteria<T> wip) {
        wip.add(produce(utility, constraint));
    }

    // ------------------------- criterion -----------------------------------------

    protected static <T> TCriterion<T> produce(TextLikeUtility<T> utility, VPattern constraint) {
        Pattern pattern = Pattern.compile(constraint.regex());
        return text -> pattern.matcher(utility.toCharSequence(text)).matches() ? null : utility.fail(text);
    }

}
