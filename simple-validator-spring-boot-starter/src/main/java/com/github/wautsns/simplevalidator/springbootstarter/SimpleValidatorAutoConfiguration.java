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
package com.github.wautsns.simplevalidator.springbootstarter;

import com.github.wautsns.simplevalidator.SimpleValidatorConfiguration;
import com.github.wautsns.simplevalidator.springbootstarter.properties.SimpleValidatorProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.PostConstruct;

/**
 * Simple validator auto-configuration.
 *
 * @author wautsns
 * @since Mar 14, 2020
 */
@RequiredArgsConstructor
@Configuration
@ComponentScan("com.github.wautsns.simplevalidator.springbootstarter.handler")
@EnableConfigurationProperties(SimpleValidatorProperties.class)
public class SimpleValidatorAutoConfiguration {

    /** Simple validator properties. */
    private final SimpleValidatorProperties properties;

    @PostConstruct
    public void postConstruct() {
        SimpleValidatorConfiguration.ForValidationFailure.FORMATTER
                .loadMessageResources(properties.getMessageResources());
        SimpleValidatorConfiguration.ForValidationFailure.setLocaleSupplier(LocaleContextHolder::getLocale);
    }

}
