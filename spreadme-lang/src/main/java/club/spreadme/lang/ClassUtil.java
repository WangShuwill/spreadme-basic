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

package club.spreadme.lang;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class ClassUtil {

    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassUtil.class.getClassLoader();
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }
        return classLoader;
    }

    public static String getClassPath() {
        return Objects.requireNonNull(getClassLoader().getResource(StringUtil.EMPTY)).getPath();
    }

    public static Set<String> getClassPaths(String packageName, boolean isDecode) {
        String packagePath = packageName.replace(StringUtil.DOT, StringUtil.SLASH);
        final Set<String> paths = new HashSet<>();
        try {
            Enumeration<URL> resources = getClassLoader().getResources(packagePath);
            while (resources.hasMoreElements()) {
                String path = resources.nextElement().getPath();
                paths.add(isDecode ? URLDecoder.decode(path, "utf-8") : path);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }
}
