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
package com.github.wautsns.simplevalidator.kernal.criterion.util;

import com.github.wautsns.simplevalidator.kernal.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForBoolean;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForByte;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForChar;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForDouble;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForFloat;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForInt;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForLong;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.basic.CriterionForShort;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.Criteria;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForBoolean;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForByte;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForChar;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForDouble;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForFloat;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForInt;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForLong;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForNonPrimitive;
import com.github.wautsns.simplevalidator.kernal.criterion.criteria.CriteriaForShort;
import com.github.wautsns.simplevalidator.kernal.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedClass;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.kernal.node.ConstrainedParameter;
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

    /** Cache for the criterion. */
    private static final Map<ConstrainedNode, Criterion> CACHE = new ConcurrentHashMap<>(128);

    /**
     * Get criterion for the specified type.
     *
     * @param type type
     * @param <C> type of criterion
     * @return criterion for the specified type
     */
    public static <C extends Criterion> C getForType(Class<?> type) {
        return getForNode(ConstrainedClass.getInstance(type));
    }

    /**
     * Get criterion for the specified node.
     *
     * @param node node
     * @param <C> type of criterion
     * @return criterion for the specified node
     */
    @SuppressWarnings("unchecked")
    public static <C extends Criterion> C getForNode(ConstrainedNode node) {
        Criterion criterion = CACHE.get(node);
        if (criterion != null) { return (C) criterion; }
        criterion = initForNode(node);
        Criterion previousValue = CACHE.putIfAbsent(node, criterion);
        return (C) ((previousValue == null) ? criterion : previousValue);
    }

    /**
     * Initialize a criterion for the specified parameter.
     *
     * @param parameter parameter
     * @param <C> type of criterion
     * @return criterion for the specified parameter
     */
    public static <C extends Criterion> C initForParameter(Parameter parameter) {
        return initForNode(new ConstrainedParameter(parameter));
    }

    /**
     * Initialize a criterion for the specified node.
     *
     * @param node constrained node
     * @param <C> type of criterion
     * @return criterion for the specified node
     */
    @SuppressWarnings("unchecked")
    public static <C extends Criterion> C initForNode(ConstrainedNode node) {
        return (C) new NodeCriterionProducer(node).produce();
    }

    /**
     * Execute the criterion with the value.
     *
     * @param criterion criterion
     * @param value value
     * @return validation failure, or {@code null} if the value passed the validation
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ValidationFailure execute(Criterion criterion, Object value) {
        if (criterion instanceof CriterionForNonPrimitive) {
            return ((CriterionForNonPrimitive) criterion).test(value);
        } else if (criterion instanceof CriterionForPrimitive) {
            return ((CriterionForPrimitive) criterion).testWrappedPrimitiveValue(value);
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Return whether the criterion is the truth.
     *
     * @param criterion criterion
     * @return {@code true} if the criterion is the truth, otherwise {@code false}
     */
    public static boolean isTheTruth(Criterion criterion) {
        return (criterion == null) || (criterion == getTheTruth(criterion.getClass()));
    }

    /**
     * Get the truth of the specified type.
     *
     * @param type type of criterion
     * @param <C> type of criterion
     * @return the truth of the specified type
     */
    @SuppressWarnings("unchecked")
    public static <C extends Criterion> C getTheTruth(Class<C> type) {
        if (TypeUtils.isAssignableTo(type, CriterionForNonPrimitive.class)) {
            return (C) CriterionForNonPrimitive.TRUTH;
        } else if (TypeUtils.isAssignableTo(type, CriterionForInt.class)) {
            return (C) CriterionForInt.TRUTH;
        } else if (TypeUtils.isAssignableTo(type, CriterionForLong.class)) {
            return (C) CriterionForLong.TRUTH;
        } else if (TypeUtils.isAssignableTo(type, CriterionForBoolean.class)) {
            return (C) CriterionForBoolean.TRUTH;
        } else if (TypeUtils.isAssignableTo(type, CriterionForChar.class)) {
            return (C) CriterionForChar.TRUTH;
        } else if (TypeUtils.isAssignableTo(type, CriterionForByte.class)) {
            return (C) CriterionForByte.TRUTH;
        } else if (TypeUtils.isAssignableTo(type, CriterionForDouble.class)) {
            return (C) CriterionForDouble.TRUTH;
        } else if (TypeUtils.isAssignableTo(type, CriterionForFloat.class)) {
            return (C) CriterionForFloat.TRUTH;
        } else if (TypeUtils.isAssignableTo(type, CriterionForShort.class)) {
            return (C) CriterionForShort.TRUTH;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * New a criteria for the specified type.
     *
     * @param type type
     * @return criteria for the specified type
     */
    public static Criteria<?> newCriteria(Type type) {
        if (!TypeUtils.isPrimitive(type)) {
            return new CriteriaForNonPrimitive<>();
        } else if (type == int.class) {
            return new CriteriaForInt();
        } else if (type == long.class) {
            return new CriteriaForLong();
        } else if (type == boolean.class) {
            return new CriteriaForBoolean();
        } else if (type == char.class) {
            return new CriteriaForChar();
        } else if (type == byte.class) {
            return new CriteriaForByte();
        } else if (type == double.class) {
            return new CriteriaForDouble();
        } else if (type == float.class) {
            return new CriteriaForFloat();
        } else if (type == short.class) {
            return new CriteriaForShort();
        } else {
            throw new IllegalStateException();
        }
    }

}
