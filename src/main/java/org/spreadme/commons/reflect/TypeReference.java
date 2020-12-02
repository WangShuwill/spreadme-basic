/*
 * Copyright [4/2/20 1:56 PM] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * TypeReference
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class TypeReference<T> {

	private final Type rawType;

	protected TypeReference() {
		Type superClass = getClass().getGenericSuperclass();
		if (superClass instanceof Class<?>) {
			throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
		}
		this.rawType = ((ParameterizedType) superClass).getActualTypeArguments()[0];
	}

	public final Type getRawType() {
		return this.rawType;
	}

	@Override
	public String toString() {
		return "TypeReference{" +
				"rawType=" + rawType +
				'}';
	}
}
