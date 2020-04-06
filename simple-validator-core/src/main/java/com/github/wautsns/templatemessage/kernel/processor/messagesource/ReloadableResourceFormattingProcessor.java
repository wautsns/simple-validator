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
package com.github.wautsns.templatemessage.kernel.processor.messagesource;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Template message formatting processor for {@link ReloadableResourceBundleMessageSource reloadable resource}.
 *
 * @author wautsns
 * @since Mar 24, 2020
 */
public class ReloadableResourceFormattingProcessor
        extends MessageSourceFormattingProcessor<ReloadableResourceBundleMessageSource> {

    public ReloadableResourceFormattingProcessor(String leftDelimiter, String rightDelimiter) {
        super(leftDelimiter, rightDelimiter, new ReloadableResourceBundleMessageSource());
        getMessageSource().setDefaultEncoding("UTF-8");
    }

    /**
     * Set cache seconds.
     *
     * @param cacheSeconds cache seconds
     * @return self reference
     * @see ReloadableResourceBundleMessageSource#setCacheSeconds(int)
     */
    public ReloadableResourceFormattingProcessor setCacheSeconds(int cacheSeconds) {
        getMessageSource().setCacheSeconds(cacheSeconds);
        return this;
    }

    /**
     * Load resources.
     *
     * @param baseNames base names
     * @return self reference
     * @see ReloadableResourceBundleMessageSource#addBasenames(String...)
     */
    public ReloadableResourceFormattingProcessor loadResources(String... baseNames) {
        getMessageSource().addBasenames(baseNames);
        return this;
    }

}
