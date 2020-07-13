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
import lombok.Setter;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The constrained parameter.
 *
 * @author wautsns
 * @since Mar 19, 2020
 */
@Getter
public class ConstrainedParameter extends ConstrainedTypeContainer {

    /** Original parameter. */
    private final Parameter origin;

    /**
     * The {@code ConstrainedParameter} is <strong>root</strong>.
     *
     * @return {@code null}
     */
    @Override
    public ConstrainedNode getParent() {
        return null;
    }

    /**
     * The {@code ConstrainedParameter} is <strong>root</strong>.
     *
     * @return {@code null}
     */
    @Override
    public Criterion.Wrapper getCriterionWrapper() {
        return null;
    }

    // #################### constructor #################################################

    /**
     * Construct a constrained parameter.
     *
     * @param parameter parameter
     */
    public ConstrainedParameter(Parameter parameter) {
        super(parameterNameGenerator.apply(parameter), parameter.getAnnotatedType());
        this.origin = parameter;
    }

    // #################### internal utils ##############################################

    /**
     * parameter name generator
     *
     * <p>default: parameter(age) -> {@code "TargetClassSimpleName#targetMethodName(String,int)@age"}
     */
    @Setter
    private static Function<Parameter, String> parameterNameGenerator = parameter -> {
        Executable executable = parameter.getDeclaringExecutable();
        Class<?> clazz = executable.getDeclaringClass();
        StringBuilder name = new StringBuilder();
        name.append(clazz.getSimpleName());
        name.append(executable.getName());
        name.append(Arrays.stream(executable.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(",", "(", ")")));
        name.append('@').append(parameter.getName());
        return name.toString();
    };

}
