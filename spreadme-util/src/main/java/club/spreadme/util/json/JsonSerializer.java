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

package club.spreadme.util.json;

import club.spreadme.lang.serializer.Serializer;
import com.alibaba.fastjson.JSON;

public class JsonSerializer<T> implements Serializer<T> {

    private final Class<T> clazz;

    public JsonSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public T deSerialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return JSON.parseObject(bytes, clazz);
    }

}
