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
package com.github.wautsns.simplevalidator.model.criterion.processor;

import com.github.wautsns.simplevalidator.model.criterion.kernel.Criteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.primitive.BooleanCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.primitive.ByteCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.primitive.CharCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.primitive.DoubleCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.primitive.FloatCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.primitive.IntCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.primitive.LongCriteria;
import com.github.wautsns.simplevalidator.model.criterion.kernel.primitive.ShortCriteria;
import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;
import com.github.wautsns.simplevalidator.util.normal.ReflectionUtils;
import com.github.wautsns.simplevalidator.util.normal.TypeUtils;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

/**
 * Process utils.
 *
 * @author wautsns
 * @since Mar 11, 2020
 */
@UtilityClass
class ProcessUtils {

    /**
     * Proxy constraint.
     *
     * @param type constraint annotation type
     * @param memberValues member values
     * @return constraint
     */
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A proxyConstraint(Class<A> type, LinkedHashMap<String, Object> memberValues) {
        return (A) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class[]{type},
                new ConstraintInvocationHandler(type, memberValues));
    }

    /**
     * New criteria for the type.
     *
     * @param type type of value
     * @return criteria for the type.
     */
    public static Criteria<? extends Criterion> newCriteriaFor(Type type) {
        if (!TypeUtils.isPrimitive(type)) {
            return new TCriteria<>();
        } else if (type == int.class) {
            return new IntCriteria();
        } else if (type == long.class) {
            return new LongCriteria();
        } else if (type == boolean.class) {
            return new BooleanCriteria();
        } else if (type == char.class) {
            return new CharCriteria();
        } else if (type == byte.class) {
            return new ByteCriteria();
        } else if (type == double.class) {
            return new DoubleCriteria();
        } else if (type == short.class) {
            return new ShortCriteria();
        } else if (type == float.class) {
            return new FloatCriteria();
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Wrap criterion to suit parent node.
     *
     * @param node constrained node
     * @param criterion criterion
     * @return criterion applicable to parent node
     */
    public static Criterion wrapCriterionToSuitParentNode(ConstrainedNode node, Criterion criterion) {
        switch (node.getCategory()) {
            case FIELD:
                return criterion.wrapFieldIntoType((Field) node.getOrigin());
            case GETTER:
                return criterion.wrapGetterIntoType((Method) node.getOrigin());
            case ITERABLE_ELEMENT:
                return criterion.wrapElementIntoIterable();
            case MAP_KEY:
                return criterion.wrapKeyIntoMap();
            case MAP_VALUE:
                return criterion.wrapValueIntoMap();
            case ARRAY_COMPONENT:
                return criterion.wrapComponentIntoArray();
            case TYPE:
            case PARAMETER:
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Parse value string.
     *
     * @param valueClass target class
     * @param valueStrings value strings.
     * @param index current index
     * @return value(type of target class)
     */
    public static Object parseValueString(Class<?> valueClass, String[] valueStrings, int index) {
        if (valueClass.isArray()) {
            Class<?> componentType = valueClass.getComponentType();
            Object array = Array.newInstance(componentType, valueStrings.length);
            for (int i = 0; i < valueStrings.length; i++) {
                Object value = parseValueString(valueClass, valueStrings, i);
                Array.set(array, i, value);
            }
            return array;
        } else if (valueClass == String.class) {
            return valueStrings[index];
        } else if (valueClass == int.class) {
            return Integer.valueOf(valueStrings[index]);
        } else if (valueClass == boolean.class) {
            return Boolean.valueOf(valueStrings[index]);
        } else if (valueClass == Class.class) {
            return ReflectionUtils.getClass(valueStrings[index]);
        } else if (valueClass == long.class) {
            return Long.valueOf(valueStrings[index]);
        } else if (valueClass == byte.class) {
            return Byte.valueOf(valueStrings[index]);
        } else if (valueClass == char.class) {
            String valueString = valueStrings[index];
            if (valueString.length() == 1) {
                return valueString.charAt(0);
            }
        } else if (valueClass == short.class) {
            return Short.valueOf(valueStrings[index]);
        } else if (valueClass == double.class) {
            return Double.valueOf(valueStrings[index]);
        } else if (valueClass == float.class) {
            return Float.valueOf(valueStrings[index]);
        }
        throw new IllegalStateException();
    }

}
