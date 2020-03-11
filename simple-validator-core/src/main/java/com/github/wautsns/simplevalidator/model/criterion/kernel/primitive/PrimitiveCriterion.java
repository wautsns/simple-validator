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
package com.github.wautsns.simplevalidator.model.criterion.kernel.primitive;

import com.github.wautsns.simplevalidator.model.criterion.kernel.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.kernel.TCriterion;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.util.normal.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Criterion for primitive value.
 *
 * @param <W> type of wrapped value(eg. Integer, Boolean...)
 * @author wautsns
 * @since Mar 11, 2020
 */
public interface PrimitiveCriterion<W> extends Criterion {

    /**
     * Test wrapped value(eg. int -> {@link Integer}).
     *
     * @param wrappedValue wrapped value
     * @return validation failure, or {@code null} if the validation is passed.
     */
    ValidationFailure testWrappedValue(W wrappedValue);

    @Override
    default TCriterion<?> wrapGetterIntoType(Method getter) {
        return source -> testWrappedValue(ReflectionUtils.invoke(source, getter));
    }

    @Override
    default TCriterion<Iterable<W>> wrapElementIntoIterable() {
        return iterable -> {
            int index = 0;
            for (Iterator<W> i = iterable.iterator(); i.hasNext(); index++) {
                ValidationFailure failure = testWrappedValue(i.next());
                if (failure != null) { return failure.addIndicator(index); }
            }
            return null;
        };
    }

    @Override
    default TCriterion<Map<W, ?>> wrapKeyIntoMap() {
        return map -> {
            for (Map.Entry<W, ?> entry : map.entrySet()) {
                ValidationFailure failure = testWrappedValue(entry.getKey());
                if (failure != null) { return failure; }
            }
            return null;
        };
    }

    @Override
    default TCriterion<Map<?, W>> wrapValueIntoMap() {
        return map -> {
            for (Map.Entry<?, W> entry : map.entrySet()) {
                ValidationFailure failure = testWrappedValue(entry.getValue());
                if (failure != null) { return failure; }
            }
            return null;
        };
    }

}
