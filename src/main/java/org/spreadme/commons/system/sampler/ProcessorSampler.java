/*
 * Copyright [4/11/20 12:32 AM] [shuwei.wang (c) wswill@foxmail.com]
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
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

import org.spreadme.commons.lang.Reflect;

/**
 * Processor Sampler
 * @author shuwei.wang
 */
public class ProcessorSampler implements Sampler {

	private static final String systemCpuUsageMethod = "getSystemCpuLoad";
	private static final String processCpuUsageMethod = "getProcessCpuLoad";

	private final OperatingSystemMXBean mxBean;
	private final Reflect reflect;

	public ProcessorSampler() {
		this.mxBean = ManagementFactory.getOperatingSystemMXBean();
		this.reflect = Reflect.of(this.mxBean);
	}

	@Override
	public List<Metrics> sample() {
		List<Metrics> metricses = new ArrayList<>();
		Metrics.of(this.mxBean, OperatingSystemMXBean::getAvailableProcessors)
				.tags("system", "cpu", "count")
				.register(metricses);
		if (mxBean.getSystemLoadAverage() > 0) {
			Metrics.of(this.mxBean, OperatingSystemMXBean::getSystemLoadAverage)
					.tags("system", "load", "average")
					.register(metricses);
		}
		Metrics.of(this.reflect, r -> r.invoke(systemCpuUsageMethod).get())
				.tags("system", "cpu", "usage")
				.register(metricses);
		Metrics.of(this.reflect, r -> r.invoke(processCpuUsageMethod).get())
				.tags("process", "cpu", "usage")
				.register(metricses);
		return metricses;
	}
}
