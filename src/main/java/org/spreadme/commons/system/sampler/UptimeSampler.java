/*
 * Copyright [4/11/20 9:55 AM] [shuwei.wang (c) wswill@foxmail.com]
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

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Uptime Sampler
 * @author shuwei.wang
 */
public class UptimeSampler implements Sampler {

	private final RuntimeMXBean runtimeMXBean;

	public UptimeSampler() {
		this.runtimeMXBean = ManagementFactory.getRuntimeMXBean();
	}

	@Override
	public List<Metrics> sample() {
		List<Metrics> metricses = new ArrayList<>();
		Metrics.of(runtimeMXBean, RuntimeMXBean::getUptime)
				.tags("process", "uptime")
				.timeunit(TimeUnit.MILLISECONDS)
				.register(metricses);

		Metrics.of(runtimeMXBean, RuntimeMXBean::getStartTime)
				.tags("process", "start", "time")
				.timeunit(TimeUnit.MILLISECONDS)
				.register(metricses);
		return metricses;
	}
}
