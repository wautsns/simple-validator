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
package com.github.wautsns.simplevalidator.util.common;

import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Collection utils.
 *
 * @author wautsns
 * @since Mar 12, 2020
 */
@UtilityClass
public class CollectionUtils {

    /** class: Collections$UnmodifiableCollection */
    private static final Class<?> CLASS_UNMODIFIABLE_COLLECTION = ReflectionUtils.requireClass(
            "java.util.Collections$UnmodifiableCollection");
    /** class: Collections$SingletonList */
    private static final Class<?> CLASS_SINGLETON_LIST = ReflectionUtils.requireClass(
            "java.util.Collections$SingletonList");
    /** class: Collections$UnmodifiableMap */
    private static final Class<?> CLASS_UNMODIFIABLE_MAP = ReflectionUtils.requireClass(
            "java.util.Collections$UnmodifiableMap");
    /** class: Collections$SingletonMap */
    private static final Class<?> CLASS_SINGLETON_MAP = ReflectionUtils.requireClass(
            "java.util.Collections$SingletonMap");

    /**
     * Wrap the list into unmodifiable list.
     *
     * <p>If the list is unmodifiable, the original list will be returned.
     *
     * @param list list
     * @param <E> type of element
     * @return unmodifiable list
     */
    public static <E> List<E> unmodifiableList(List<E> list) {
        if (list.size() > 1) {
            if (CLASS_UNMODIFIABLE_COLLECTION.isInstance(list)) {
                return list;
            } else {
                return Collections.unmodifiableList(list);
            }
        } else if (list.isEmpty()) {
            return Collections.emptyList();
        } else if (CLASS_SINGLETON_LIST.isInstance(list)) {
            return list;
        } else {
            return Collections.singletonList(list.get(0));
        }
    }

    /**
     * Wrap the map into unmodifiable map.
     *
     * <p>If the map is unmodifiable, the original map will be returned.
     *
     * @param map map
     * @param <K> type of key
     * @param <V> type of value
     * @return unmodifiable map
     */
    public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
        if (map.size() > 1) {
            if (CLASS_UNMODIFIABLE_MAP.isInstance(map)) {
                return map;
            } else {
                return Collections.unmodifiableMap(map);
            }
        } else if (map.isEmpty()) {
            return Collections.emptyMap();
        } else if (CLASS_SINGLETON_MAP.isInstance(map)) {
            return map;
        } else {
            Map.Entry<K, V> entry = map.entrySet().iterator().next();
            return Collections.singletonMap(entry.getKey(), entry.getValue());
        }
    }

    public static <K, V> V removeAndGet(Map<K, V> map, K key, V defaultValue) {
        V value = map.remove(key);
        return (value != null) ? value : defaultValue;
    }

}