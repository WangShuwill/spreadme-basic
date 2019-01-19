/*
 *  Copyright (c) 2019 Wangshuwei
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package club.spreadme.lang.properties;

import club.spreadme.lang.ClassUtil;
import club.spreadme.lang.StringUtil;
import club.spreadme.lang.cache.Cache;
import club.spreadme.lang.cache.support.ConcurrentMapCache;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class PropertyUtil {

    public static Cache<String, Map<Object, Object>> propertiesCache = new ConcurrentMapCache<>("PROPERTIES_CACHE");
    public static final String PROPERTIES_EXT = ".properties";

    public static void loadProperties() {
        String classPath = ClassUtil.getClassPath();
        Path propertyPath = FileSystems.getDefault().getPath(classPath);
        try {
            Files.walk(propertyPath).filter(childPath -> childPath.toFile().isFile() && isPropertiesFile(childPath.toString()))
                    .forEach(path -> {
                        try {
                            File propertyFile = path.toFile();
                            Map<Object, Object> propertyMap = loadProperties(new FileInputStream(propertyFile));
                            propertiesCache.putIfAbsent(StringUtil.trimEnd(propertyFile.getName(), PROPERTIES_EXT), propertyMap);
                        }
                        catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadProperties(String filePath) {
        File propertyFile = new File(filePath);
        try {
            Map<Object, Object> propertyMap = loadProperties(new FileInputStream(propertyFile));
            String cacheKey = StringUtil.trimEnd(propertyFile.getName(), PROPERTIES_EXT);
            propertiesCache.putIfAbsent(cacheKey, propertyMap);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected static Map<Object, Object> loadProperties(InputStream is) {
        Properties properties = new Properties();
        Map<Object, Object> propertyMap = new HashMap<>();
        try {
            properties.load(is);
            Enumeration enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                Object key = enumeration.nextElement();
                Object value = properties.get(key);
                propertyMap.put(key, value);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return propertyMap;
    }

    public static Object getValue(String propertyFileName, String key) {
        return propertiesCache.get(propertyFileName).get(key);
    }

    public static <T> T getValue(String propertyFileName, String key, Class<T> clazz) {
        return null;
    }

    public static boolean isPropertiesFile(String fullPath) {
        return Objects.equals(PROPERTIES_EXT, fullPath.substring(fullPath.lastIndexOf(".")));
    }


}
