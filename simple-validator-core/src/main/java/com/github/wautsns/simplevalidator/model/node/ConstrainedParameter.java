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

import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import lombok.Getter;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Constrained parameter node.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@Getter
public class ConstrainedParameter extends ConstrainedType {

    /** original parameter */
    private final Parameter origin;

    /**
     * The {@code ConstrainedParameter} is root node.
     *
     * @return {@code null}
     */
    @Override
    public ConstrainedNode getParent() {
        return null;
    }

    /**
     * The {@code ConstrainedParameter} is root node.
     *
     * @return {@code null}
     */
    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return null;
    }

    public ConstrainedParameter(Parameter parameter) {
        super(parameter.getParameterizedType(), new Location(getName(parameter)), parameter.getAnnotatedType());
        this.origin = parameter;
    }

    /**
     * Get parameter name is the name of type of parameter, like 'TargetClass#targetMethod(String,int)@age'.
     *
     * @param parameter parameter
     * @return parameter name
     */
    public static String getName(Parameter parameter) {
        Executable executable = parameter.getDeclaringExecutable();
        Class<?> clazz = executable.getDeclaringClass();
        StringBuilder result = new StringBuilder();
        result.append(clazz.getSimpleName());
        result.append('#').append(executable.getName());
        result.append(Arrays.stream(executable.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(",", "(", ")")));
        result.append('@').append(parameter.getName());
        return result.toString();
    }

}
