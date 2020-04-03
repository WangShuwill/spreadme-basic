/*
 * Copyright [4/3/20 2:24 PM] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.lang;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Annotation Util
 * @author shuwei.wang
 */
public class Annotate {

	private final Annotation annotation;
	private final AnnotatedElement element;

	private Annotate(AnnotatedElement element, Class<? extends Annotation> annotationType) {
		this.element = element;
		this.annotation = this.element.getAnnotation(annotationType);
	}

	public static Annotate of(AnnotatedElement element, Class<? extends Annotation> annotationType) {
		return new Annotate(element, annotationType);
	}

	@SuppressWarnings("unchecked")
	public Definition definition() {
		Definition definition = new Definition();
		InvocationHandler invocationHandler = Proxy.getInvocationHandler(this.annotation);
		Map<String, Reflect> reflectMap = Reflect.of(invocationHandler).fields();
		for (Map.Entry<String, Reflect> entry : reflectMap.entrySet()) {
			Reflect reflect = entry.getValue();
			if (reflect.type().equals(Class.class)) {
				definition.setType((Class<? extends Annotation>) reflect.type());
			}
			else if (reflect.type().equals(Map.class)) {
				definition.setAttributes(reflect.get());
			}
		}
		return definition;
	}

	public Map<String, Object> attributes() {
		return this.definition().getAttributes();
	}

	@SuppressWarnings("unchecked")
	public <T> T value(String key) {
		Map<String, Object> attributes = this.definition().getAttributes();
		if (attributes != null) {
			return (T) attributes.get(key);
		}
		return null;
	}

	public Map<String, Annotate> anntations() {
		Map<String, Annotate> result = new LinkedHashMap<>();
		Annotation[] annotations = this.element.getAnnotations();
		for (Annotation annotation : annotations) {
			result.put(annotation.annotationType().getName(), of(this.element, annotation.annotationType()));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T extends Annotation> T get() {
		return (T) this.annotation;
	}

	public static class Definition {

		private Class<? extends Annotation> type;

		private Map<String, Object> attributes;

		public Definition() {

		}

		public Definition(Class<? extends Annotation> type, Map<String, Object> attributes) {
			this.type = type;
			this.attributes = attributes;
		}

		public Class<? extends Annotation> getType() {
			return type;
		}

		public void setType(Class<? extends Annotation> type) {
			this.type = type;
		}

		public Map<String, Object> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, Object> attributes) {
			this.attributes = attributes;
		}

		@Override
		public String toString() {
			return "AnnotateDefinition{" +
					"type=" + type +
					", attributes=" + attributes +
					'}';
		}
	}
}
