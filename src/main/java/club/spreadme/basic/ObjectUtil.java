/*
 *  Copyright (c) 2018 Wangshuwei
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

package club.spreadme.basic;

import java.util.Arrays;

public class ObjectUtil {

    public static int hashcode(Object object) {
        if (object == null) {
            return 0;
        }
        final Class<?> clazz = object.getClass();
        if (!clazz.isArray()) {
            return object.hashCode();
        }
        final Class<?> componentType = clazz.getComponentType();
        if (long.class.equals(componentType)) {
            return Arrays.hashCode((long[]) object);
        }
        else if (int.class.equals(componentType)) {
            return Arrays.hashCode((int[]) object);
        }
        else if (short.class.equals(componentType)) {
            return Arrays.hashCode((short[]) object);
        }
        else if (char.class.equals(componentType)) {
            return Arrays.hashCode((char[]) object);
        }
        else if (byte.class.equals(componentType)) {
            return Arrays.hashCode((byte[]) object);
        }
        else if (boolean.class.equals(componentType)) {
            return Arrays.hashCode((boolean[]) object);
        }
        else if (float.class.equals(componentType)) {
            return Arrays.hashCode((float[]) object);
        }
        else if (double.class.equals(componentType)) {
            return Arrays.hashCode((double[]) object);
        }
        else {
            return Arrays.hashCode((Object[]) object);
        }
    }

}
