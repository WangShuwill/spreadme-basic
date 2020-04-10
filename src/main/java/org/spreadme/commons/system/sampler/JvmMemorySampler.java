/*
 * Copyright [4/11/20 1:10 AM] [shuwei.wang (c) wswill@foxmail.com]
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

import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.List;

import org.spreadme.commons.lang.SizeUnit;
import org.spreadme.commons.util.StringUtil;

/**
 * JvmMemory Sampler
 * @author shuwei.wang
 */
public class JvmMemorySampler implements Sampler {

	@Override
	public List<Metrics> sample() {
		List<Metrics> metricses = new ArrayList<>();
		for (BufferPoolMXBean bean : ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)) {
			String name = bean.getName();
			Metrics.of(bean, BufferPoolMXBean::getCount)
					.tags(name, "buffer", "count")
					.register(metricses);
			Metrics.of(bean, BufferPoolMXBean::getMemoryUsed)
					.tags(name, "buffer", "memory", "used")
					.sizeunit(SizeUnit.B)
					.register(metricses);
			Metrics.of(bean, BufferPoolMXBean::getTotalCapacity)
					.tags(name, "buffer", "total", "capacity")
					.sizeunit(SizeUnit.B)
					.register(metricses);
		}
		for (MemoryPoolMXBean bean : ManagementFactory.getPlatformMXBeans(MemoryPoolMXBean.class)) {
			String name = StringUtil.trimAll(bean.getName()).toLowerCase();
			Metrics.of(bean, (mem) -> mem.getUsage().getUsed())
					.tags(name, "memory", "used")
					.sizeunit(SizeUnit.B)
					.register(metricses);

			Metrics.of(bean, (mem) -> mem.getUsage().getCommitted())
					.tags(name, "memory", "committed")
					.sizeunit(SizeUnit.B)
					.register(metricses);

			Metrics.of(bean, (mem) -> mem.getUsage().getMax())
					.tags(name, "memory", "max")
					.sizeunit(SizeUnit.B)
					.register(metricses);
		}
		return metricses;
	}
}
