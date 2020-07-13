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
package com.github.wautsns.simplevalidator.springbootstarter.handler.validation;

import com.github.wautsns.simplevalidator.SimpleValidatorConfiguration;
import com.github.wautsns.simplevalidator.model.constraint.ConstraintMetadata;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * Simple method validation post processor.
 *
 * @author wautsns
 * @see MethodValidationPostProcessor
 * @since Mar 14, 2020
 */
@Component
public class SimpleMethodValidationPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor
        implements InitializingBean {

    private static final long serialVersionUID = -8255787111281581563L;

    @Override
    public void afterPropertiesSet() {
        SimpleValidatorConfiguration.ForValidationFailure.setLocaleSupplier(LocaleContextHolder::getLocale);
        this.advisor = new DefaultPointcutAdvisor(POINTCUT, new SimpleMethodValidationInterceptor());
    }

    /** Pointcut for the post processor. */
    private static final Pointcut POINTCUT = new Pointcut() {

        /**Method matcher for the pointcut. */
        private final MethodMatcher methodMatcher = new MethodMatcher() {

            @Override
            public boolean matches(Method method, Class<?> targetClass, Object... args) {
                throw new UnsupportedOperationException("The MethodMatcher is not dynamic");
            }

            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                int modifiers = method.getModifiers();
                if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) { return false; }
                return Arrays.stream(method.getParameters())
                        .map(Parameter::getDeclaredAnnotations)
                        .flatMap(Arrays::stream)
                        .map(Annotation::annotationType)
                        .anyMatch(ConstraintMetadata::isConstraintType);
            }

            @Override
            public boolean isRuntime() {
                return false;
            }

        };

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return methodMatcher;
        }

    };

}
