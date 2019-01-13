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

package club.spreadme.lang.serializer.support;

import club.spreadme.lang.serializer.Serializer;

import java.io.*;

public class DefaultSerializer<T> implements Serializer<T> {

    @Override
    public byte[] serialize(T object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(object);
            return bos.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deSerialize(byte[] bytes) {
        if(bytes == null){
            return null;
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            return (T) ois.readObject();
        }
        catch (IOException e) {
            e.getMessage();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
