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
package com.github.wautsns.simplevalidator.util;

import com.github.wautsns.simplevalidator.model.criterion.basic.BooleanCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.ByteCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.CharCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.DoubleCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.FloatCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.IntCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.LongCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.PrimitiveCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.ShortCriterion;
import com.github.wautsns.simplevalidator.model.criterion.basic.TCriterion;
import com.github.wautsns.simplevalidator.model.criterion.processor.NodeCriterionProducer;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedClass;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.model.node.ConstrainedParameter;
import com.github.wautsns.simplevalidator.util.common.TypeUtils;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Criterion utils.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@UtilityClass
public class CriterionUtils {

    /** cache for criterion */
    private static final Map<ConstrainedNode, Criterion> CACHE = new ConcurrentHashMap<>(128);

    /**
     * Execute the criterion with the value.
     *
     * @param criterion criterion
     * @param value value
     * @return validation failure, or {@code null} if the value passed the validation
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ValidationFailure execute(Criterion criterion, Object value) {
        if (criterion instanceof TCriterion) {
            return ((TCriterion) criterion).test(value);
        } else if (criterion instanceof PrimitiveCriterion) {
            return ((PrimitiveCriterion) criterion).testWrappedValue(value);
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Get criterion for the specific class.
     *
     * @param clazz class
     * @param <C> type of criterion
     * @return criterion for the specific class
     */
    public static <C extends Criterion> C forClass(Class<?> clazz) {
        return forNode(ConstrainedClass.getInstance(clazz));
    }

    /**
     * Create a criterion for the specific parameter.
     *
     * @param parameter parameter
     * @param <C> type of criterion
     * @return criterion for the specific parameter
     */
    @SuppressWarnings("unchecked")
    public static <C extends Criterion> C forParameter(Parameter parameter) {
        return (C) resolveNode(new ConstrainedParameter(parameter));
    }

    /**
     * Get criterion for the specific node.
     *
     * @param node node
     * @param <C> type of criterion
     * @return criterion for the specific node
     */
    @SuppressWarnings("unchecked")
    public static <C extends Criterion> C forNode(ConstrainedNode node) {
        return (C) CACHE.computeIfAbsent(node, CriterionUtils::resolveNode);
    }

    /**
     * Resolve node.
     *
     * @param node node
     * @return criterion for the specific node
     */
    private static Criterion resolveNode(ConstrainedNode node) {
        Criterion criterion = new NodeCriterionProducer(node).produce();
        if (criterion != null) { return criterion; }
        Type type = node.getType();
        if (!TypeUtils.isPrimitive(type)) {
            return TCriterion.TRUTH;
        } else if (type == int.class) {
            return IntCriterion.TRUTH;
        } else if (type == boolean.class) {
            return BooleanCriterion.TRUTH;
        } else if (type == long.class) {
            return LongCriterion.TRUTH;
        } else if (type == char.class) {
            return CharCriterion.TRUTH;
        } else if (type == double.class) {
            return DoubleCriterion.TRUTH;
        } else if (type == byte.class) {
            return ByteCriterion.TRUTH;
        } else if (type == short.class) {
            return ShortCriterion.TRUTH;
        } else if (type == float.class) {
            return FloatCriterion.TRUTH;
        } else {
            throw new IllegalStateException();
        }
    }

}
