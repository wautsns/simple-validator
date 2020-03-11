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

import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.TypeLikeUtilityCriterionCache;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.text.AbstractTextLikeCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.text.TextLikeUtility;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriterion;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

/**
 * @author wautsns
 * @since Mar 11, 2020
 */
public class VNotBlankTextLikeCriterionFactory extends AbstractTextLikeCriterionFactory<VNotBlank> {

    @Override
    protected <T> void process(
            TextLikeUtility<T> utility,
            ConstrainedNode element, VNotBlank constraint, TCriteria<T> wip) {
        wip.add(produce(utility));
    }

    // ------------------------- criterion -----------------------------------------

    protected static <T> TCriterion<T> produce(TextLikeUtility<T> utility) {
        return CACHE.get(utility);
    }

    private static final TypeLikeUtilityCriterionCache<TextLikeUtility<?>> CACHE =
            new TypeLikeUtilityCriterionCache<>(VNotBlankTextLikeCriterionFactory::init);

    private static <T> TCriterion<T> init(TextLikeUtility<T> utility) {
        return text -> {
            for (int i = 0, l = utility.length(text); i < l; i++) {
                if (!Character.isWhitespace(utility.charAt(text, i))) {
                    return null;
                }
            }
            return utility.fail(text);
        };
    }

}
