/*
 * Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package club.spreadme.core.utils;

import java.io.File;
import java.io.IOException;

public abstract class FileUtil {

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    private static final String TEMP_PATH = System.getProperty("java.io.tmpdir");
    private static final String USER_PATH = System.getProperty("user.home");

    public static String getTempPath() {
        return TEMP_PATH;
    }

    public static File getTempFile() {
        return new File(getTempPath());
    }

    public static String getUserPath() {
        return USER_PATH;
    }

    public static File getUserFile() {
        return new File(getUserPath());
    }

    public static String getFilename(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    public static String getFilenameExtension(String path) {
        if (path == null) {
            return null;
        }

        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }

        return path.substring(extIndex + 1);
    }

    public static File createFile(String path, boolean isFile) {
        path = path.replaceAll("\\*", "/");
        File file = new File(path);
        if (!file.exists()) {
            try {
                if (isFile) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                    return file;
                }
                else if (file.mkdirs()) {
                    return file;
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }
}
