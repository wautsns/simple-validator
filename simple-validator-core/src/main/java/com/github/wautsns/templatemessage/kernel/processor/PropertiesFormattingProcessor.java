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
import lombok.Cleanup;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Template message formatting processor for {@linkplain java.util.Properties properties}.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
@EqualsAndHashCode(callSuper = true)
public class PropertiesFormattingProcessor extends TemplateMessageFormatter.Processor {

    /** serialVersionUID */
    private static final long serialVersionUID = -5669893817925948919L;

    /** locale properties map */
    private final Map<Locale, Properties> data = new ConcurrentHashMap<>();

    public PropertiesFormattingProcessor(String leftDelimiter, String rightDelimiter) {
        super(leftDelimiter, rightDelimiter);
    }

    @Override
    public String process(String text, VariableValueMap variableValueMap, Locale locale) {
        Properties properties = data.get(locale);
        if (properties != null) { return properties.getProperty(text); }
        if (!locale.getVariant().isEmpty()) {
            properties = data.get(new Locale(locale.getLanguage(), locale.getCountry()));
            if (properties == null) { properties = data.get(new Locale(locale.getLanguage())); }
        } else if (!locale.getCountry().isEmpty()) {
            properties = data.get(new Locale(locale.getLanguage()));
        }
        if (properties == null) { return null; }
        data.put(locale, properties);
        return properties.getProperty(text);
    }

    /**
     * Load properties.
     *
     * @param locale locale
     * @param properties properties
     * @return self reference
     */
    public PropertiesFormattingProcessor load(Locale locale, Properties properties) {
        data.computeIfAbsent(locale, i -> new Properties()).putAll(properties);
        return this;
    }

    /**
     * Load properties with properties input stream.
     *
     * @param locale locale
     * @param inputStream properties input stream
     * @return self reference
     */
    @SneakyThrows
    public PropertiesFormattingProcessor load(Locale locale, InputStream inputStream) {
        Properties properties = new Properties();
        @Cleanup InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        properties.load(reader);
        return load(locale, properties);
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
    @SneakyThrows
    public PropertiesFormattingProcessor load(String folderPath, String baseName) {
        URL url = getClass().getClassLoader().getResource(folderPath);
        Objects.requireNonNull(url, String.format("Folder path[%s] does not exist.", folderPath));
        switch (url.getProtocol()) {
            case "file":
                File directory = new File(url.toURI());
                return load(directory, baseName);
            case "jar":
                String jarUrlString = url.toString();
                url = new URL(jarUrlString.substring(0, jarUrlString.lastIndexOf("!/") + 2));
                JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                return load(jarURLConnection.getJarFile(), folderPath, baseName);
            default:
                throw new IllegalStateException("Unsupported protocol: " + url.getProtocol());
        }
    }

    @SneakyThrows
    public PropertiesFormattingProcessor load(JarFile jarFile, String folderPath, String baseName) {
        Enumeration<JarEntry> entries = jarFile.entries();
        String prefix = folderPath + baseName;
        ClassLoader classLoader = getClass().getClassLoader();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.startsWith(prefix) && isEndsWithDotProperties(name)) {
                int lastIndexOfSlash = name.lastIndexOf('/');
                if (lastIndexOfSlash >= prefix.length()) { continue; }
                String fileName = name.substring(lastIndexOfSlash + 1);
                Locale locale = getLocaleByFileName(fileName, baseName);
                @Cleanup InputStream inputStream = classLoader.getResourceAsStream(name);
                load(locale, inputStream);
            }
        }
        return this;
    }

    @SneakyThrows
    public PropertiesFormattingProcessor load(File directory, String baseName) {
        File[] files = directory.listFiles((f, n) -> n.startsWith(baseName) && isEndsWithDotProperties(n));
        for (File file : Objects.requireNonNull(files)) {
            @Cleanup FileInputStream fis = new FileInputStream(file);
            Properties properties = new Properties();
            @Cleanup InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            properties.load(reader);
            String fileName = file.getName();
            String langTag = fileName.substring(baseName.length() + 1, fileName.lastIndexOf('.'));
            Locale locale = Locale.forLanguageTag(langTag);
            load(locale, properties);
        }
        return this;
    }

    private static boolean isEndsWithDotProperties(String fileName) {
        return fileName.endsWith(".properties");
    }

    private static Locale getLocaleByFileName(String fileName, String baseName) {
        String langTag = fileName.substring(baseName.length() + 1, fileName.length() - ".properties".length());
        return Locale.forLanguageTag(langTag);
    }

}
