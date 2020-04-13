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

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.ToDoubleFunction;

import org.spreadme.commons.lang.SizeUnit;

/**
 * Metrics
 * @author shuwei.wang
 */
public class Metrics implements Serializable {

	private static final long serialVersionUID = -6523823373030440648L;

	private final Double value;
	private final Long timestamp;

	private String name;
	private String description;
	private SizeUnit sizeunit;
	private TimeUnit timeunit;

	private Metrics(Double value) {
		this.value = value;
		this.timestamp = System.currentTimeMillis();
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

	public Metrics sizeunit(SizeUnit sizeunit) {
		this.sizeunit = sizeunit;
		return this;
	}

	public Metrics timeunit(TimeUnit timeunit) {
		this.timeunit = timeunit;
		return this;
	}

	public void register(List<Metrics> metricses) {
		metricses.add(this);
	}

	public String name() {
		return this.name;
	}

	public Double value() {
		return this.value;
	}

	public Long timestamp() {
		return this.timestamp;
	}

	public String description() {
		return this.description;
	}

	public SizeUnit sizeunit() {
		return this.sizeunit;
	}

	public TimeUnit timeunit() {
		return this.timeunit;
	}

	@Override
	public String toString() {
		return "Metrics{" +
				"name='" + name + '\'' +
				", value=" + value +
				", sizeUnit=" + sizeunit +
				", timeUnit=" + timeunit +
				'}';
	}
}
