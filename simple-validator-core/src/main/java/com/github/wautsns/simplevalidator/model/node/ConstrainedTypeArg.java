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
package com.github.wautsns.simplevalidator.model.node;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Abstract constrained type arg node.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Getter
public abstract class ConstrainedTypeArg extends ConstrainedType {

    /** parent node */
    protected final ConstrainedType parent;

    protected ConstrainedTypeArg(
            ConstrainedType parent, Type type, String name,
            List<Short> indexes, Map<List<Short>, List<Annotation>> indexesConstraints) {
        super(type, new Location(parent, name), indexes, indexesConstraints);
        this.parent = parent;
    }

    /**
     * type arg node factory
     *
     * @see ConstrainedType.TypeArgsFactories
     */
    public interface Factory {

        /**
         * Get target class.
         *
         * @return target class
         */
        Class<?> getTargetClass();

        /**
         * Get type arg index.
         *
         * @return type arg index
         */
        short getTypeArgIndex();

        /**
         * Produce constrained type.
         *
         * @param parent parent node
         * @param type type
         * @param indexes indexes of the type
         * @param indexesConstraints indexes constraints
         * @return constrained type
         */
        ConstrainedTypeArg produce(
                ConstrainedType parent, Type type, List<Short> indexes,
                Map<List<Short>, List<Annotation>> indexesConstraints);

    }

}
