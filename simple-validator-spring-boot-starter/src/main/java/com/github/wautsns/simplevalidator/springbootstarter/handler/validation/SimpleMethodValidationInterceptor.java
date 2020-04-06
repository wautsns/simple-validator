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
package com.github.wautsns.simplevalidator.springbootstarter.handler.validation;

import com.github.wautsns.simplevalidator.exception.ValidationException;
import com.github.wautsns.simplevalidator.model.criterion.basic.Criterion;
import com.github.wautsns.simplevalidator.model.criterion.util.CriterionUtils;
import com.github.wautsns.simplevalidator.model.failure.ValidationFailure;
import com.github.wautsns.simplevalidator.model.node.ConstrainedParameter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.util.ClassUtils;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wautsns
 * @see MethodValidationInterceptor
 * @since Mar 14, 2020
 */
public class SimpleMethodValidationInterceptor implements MethodInterceptor {

    private final Map<Method, Criterion[]> cache = new ConcurrentHashMap<>(128);

    @Override
    public Object invoke(MethodInvocation invocation) throws ValidationException, Throwable {
        Method method = invocation.getMethod();
        // Avoid Validator invocation on FactoryBean.getObjectType/isSingleton
        if (isFactoryBeanMetadataMethod(method)) { return invocation.proceed(); }
        Criterion[] parameterCriterionArray = cache.computeIfAbsent(method, this::initParametersCriterion);
        Object[] arguments = invocation.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            Criterion criterion = parameterCriterionArray[i];
            if (criterion == null) { continue; }
            ValidationFailure failure = CriterionUtils.execute(criterion, arguments[i]);
            if (failure != null) { throw new ValidationException(failure); }
        }
        return invocation.proceed();
    }

    private Criterion[] initParametersCriterion(Method method) {
        return Arrays.stream(method.getParameters())
                .map(ConstrainedParameter::new)
                .map(CriterionUtils::produce)
                .toArray(Criterion[]::new);
    }

    private boolean isFactoryBeanMetadataMethod(Method method) {
        Class<?> clazz = method.getDeclaringClass();

        // Call from interface-based proxy handle, allowing for an efficient check?
        if (clazz.isInterface()) {
            return ((clazz == FactoryBean.class || clazz == SmartFactoryBean.class) &&
                    !"getObject".equals(method.getName()));
        }
        // Call from CGLIB proxy handle, potentially implementing a FactoryBean method?
        Class<?> factoryBeanType = null;
        if (SmartFactoryBean.class.isAssignableFrom(clazz)) {
            factoryBeanType = SmartFactoryBean.class;
        } else if (FactoryBean.class.isAssignableFrom(clazz)) {
            factoryBeanType = FactoryBean.class;
        }
        return (factoryBeanType != null && !"getObject".equals(method.getName()) &&
                ClassUtils.hasMethod(factoryBeanType, method.getName(), method.getParameterTypes()));
    }

}
