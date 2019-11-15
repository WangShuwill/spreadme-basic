/*
 * Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.io.serialize;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.spreadme.commons.io.FastByteArrayOutputStream;

/**
 * Object Serializer
 * @author shuwei.wang
 */
public class ObjectSerializer<T> implements Serializer {

	@Override
	public byte[] serialize(Object object) throws SerializeException {
		try (FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
			 ObjectOutputStream oos = new ObjectOutputStream(bos)) {

			oos.writeObject(object);
			return bos.toByteArray();
		}
		catch (IOException ex) {
			throw new SerializeException(ex.getMessage(), ex);
		}
	}

	@Override
	public Object deserialize(byte[] bytes) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			 ObjectInputStream ois = new ObjectInputStream(bis)) {

			return ois.readObject();
		}
		catch (Exception ex) {
			throw new SerializeException(ex.getMessage(), ex);
		}
	}
}
