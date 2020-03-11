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
package com.github.wautsns.simplevalidator.model.criterion.factory.typelike.text;

import com.github.wautsns.simplevalidator.model.criterion.factory.typelike.TypeLikeCriterionFactory;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriteria;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract text like criterion factory.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
public abstract class AbstractTextLikeCriterionFactory<A extends Annotation>
        extends TypeLikeCriterionFactory<A, TextLikeUtility<?>> {

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void process(ConstrainedNode node, A constraint, TCriteria<Object> wip) {
        TextLikeUtility utility = getUtility(node.getType());
        process(utility, node, constraint, wip);
    }

    /**
     * Process wip of criteria.
     *
     * @param utility text like utility
     * @param node node
     * @param constraint constraint
     * @param wip wip of criteria
     */
    protected abstract <T> void process(
            TextLikeUtility<T> utility, ConstrainedNode node, A constraint, TCriteria<T> wip);

    @Override
    protected List<TextLikeUtility<?>> initUtilities() {
        return Arrays.asList(
                CharSequenceUtility.DEFAULT,
                CharArrayUtility.DEFAULT,
                CharacterArrayUtility.DEFAULT);
    }

}
