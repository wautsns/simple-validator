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
package com.github.wautsns.templatemessage.kernel.processor;

import com.github.wautsns.templatemessage.kernel.TemplateMessageFormatter;
import com.github.wautsns.templatemessage.variable.VariableValueMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Template message formatting processor for {@linkplain java.util.Properties properties}.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
public class PropertiesFormattingProcessor extends TemplateMessageFormatter.Processor {

    /** serialVersionUID */
    private static final long serialVersionUID = -5669893817925948919L;

    /** locale properties map */
    private final Map<Locale, Properties> localePropertiesMap = new ConcurrentHashMap<>();

    public PropertiesFormattingProcessor(String leftDelimiter, String rightDelimiter) {
        super(leftDelimiter, rightDelimiter);
    }

    @Override
    public String process(String name, VariableValueMap variableValueMap, Locale locale) {
        Properties properties = localePropertiesMap.get(locale);
        if (properties != null) { return properties.getProperty(name); }
        if (!locale.getVariant().isEmpty()) {
            properties = localePropertiesMap.get(new Locale(locale.getLanguage(), locale.getCountry()));
            if (properties == null) { properties = localePropertiesMap.get(new Locale(locale.getLanguage())); }
        } else if (!locale.getCountry().isEmpty()) {
            properties = localePropertiesMap.get(new Locale(locale.getLanguage()));
        }
        if (properties == null) { return null; }
        localePropertiesMap.put(locale, properties);
        return properties.getProperty(name);
    }

    /**
     * Load properties.
     *
     * <p>eg. load("/i18n", "messages") -> /i18n/messages_zh.properties; /i18n/messages_en.properties
     *
     * @param folderPath folder path
     * @param baseName base name of properties
     * @return self reference
     */
    public PropertiesFormattingProcessor load(String folderPath, String baseName) {
        try {
            URL url = PropertiesFormattingProcessor.class.getClassLoader().getResource(folderPath);
            File folder = new File(Objects.requireNonNull(url).toURI());
            FilenameFilter filter = (f, n) -> n.startsWith(baseName) && n.endsWith(".properties");
            for (File file : Objects.requireNonNull(folder.listFiles(filter))) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    Properties properties = new Properties();
                    InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                    properties.load(reader);
                    reader.close();
                    String fileName = file.getName();
                    int begin = baseName.length() + 1;
                    int end = fileName.lastIndexOf('.');
                    String langTag = fileName.substring(begin, end);
                    Locale locale = langTag.isEmpty() ? null : Locale.forLanguageTag(langTag);
                    load(locale, properties);
                }
            }
            return this;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Load properties.
     *
     * @param locale locale
     * @param properties properties
     * @return self reference
     */
    public PropertiesFormattingProcessor load(Locale locale, Properties properties) {
        localePropertiesMap.computeIfAbsent(locale, i -> new Properties()).putAll(properties);
        return this;
    }

}
