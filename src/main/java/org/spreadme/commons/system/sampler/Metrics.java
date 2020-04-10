/*
 * Copyright [4/11/20 12:28 AM] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.system.sampler;

import java.util.List;
import java.util.function.ToDoubleFunction;

import org.spreadme.commons.lang.SizeUnit;

/**
 * Metrics
 * @author shuwei.wang
 */
public class Metrics {

	private final Double value;

	private String name;
	private String description;
	private SizeUnit sizeUnit;

	private Metrics(Double value) {
		this.value = value;
	}

	public static <T> Metrics of(T t, ToDoubleFunction<T> f) {
		return new Metrics(f.applyAsDouble(t));
	}

	public Metrics tags(String... tags) {
		this.name = String.join(".", tags);
		return this;
	}

	public Metrics description(String description) {
		this.description = description;
		return this;
	}

	public Metrics sizeunit(SizeUnit sizeUnit) {
		this.sizeUnit = sizeUnit;
		return this;
	}

	public void register(List<Metrics> metricses) {
		metricses.add(this);
	}

	public String name() {
		return name;
	}

	public Double value() {
		return value;
	}

	public String description() {
		return description;
	}

	public SizeUnit sizeunit() {
		return sizeUnit;
	}

	@Override
	public String toString() {
		return "Metrics{" +
				"name='" + name + '\'' +
				", value=" + value +
				", sizeUnit=" + sizeUnit +
				'}';
	}
}
