/*
 *    Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.spreadme.commons.sys;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

/**
 * monitor service
 * @author shuwei.wang
 * @since 1.0.0
 */
public class SystemMonitor {

	private static Runtime runtime = Runtime.getRuntime();

	private static final String OS_NAME_KEY = "os.name";

	private static final String[] UNITS = new String[] {"", "K", "M", "G", "T", "P", "E"};

	private static OSType osType;

	static {
		osType = OSType.resolve(System.getProperty(OS_NAME_KEY));
	}

	public SystemInfo getSystemInfo() {
		SystemInfo systemInfo = new SystemInfo();
		// jvm
		systemInfo.setTotalJvmMemory(runtime.totalMemory());
		systemInfo.setTotalJvmMemoryUnit(lengthToUnit(systemInfo.getTotalJvmMemory()));
		systemInfo.setFreeJvmMemory(runtime.freeMemory());
		systemInfo.setFreeJvmMemoryUnit(lengthToUnit(systemInfo.getFreeJvmMemory()));
		systemInfo.setMaxJvmMemory(runtime.maxMemory());
		systemInfo.setMaxJvmMemoryUnit(lengthToUnit(systemInfo.getMaxJvmMemory()));
		// os
		OperatingSystemMXBean osmxbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		systemInfo.setTotalMemory(osmxbean.getTotalPhysicalMemorySize());
		systemInfo.setTotalMemoryUnit(lengthToUnit(systemInfo.getTotalMemory()));

		systemInfo.setFreeMemory(osmxbean.getFreePhysicalMemorySize());
		systemInfo.setFreeMemoryUnit(lengthToUnit(systemInfo.getFreeMemory()));

		systemInfo.setUsedMemory(systemInfo.getTotalMemory() - systemInfo.getFreeJvmMemory());
		systemInfo.setUsedMemoryUnit(lengthToUnit(systemInfo.getUsedMemory()));

		systemInfo.setProcessors(osmxbean.getAvailableProcessors());
		String osName = System.getProperty(OS_NAME_KEY);
		systemInfo.setOsName(osName);
		systemInfo.setOsType(osType);
		systemInfo.setArch(osmxbean.getArch());
		systemInfo.setVersion(osmxbean.getVersion());
		systemInfo.setCpuLoad(osmxbean.getProcessCpuLoad());
		systemInfo.setCpuTime(osmxbean.getProcessCpuTime());
		return systemInfo;
	}

	public String lengthToUnit(long length) {
		for (int i = 6; i > 0; i--) {
			// 1024 is for 1024 kb is 1 MB etc
			double step = Math.pow(1024, i);
			if (length > step) {
				return String.format("%3.1f%s", length / step, UNITS[i]);
			}
		}
		return Long.toString(length);
	}
}
