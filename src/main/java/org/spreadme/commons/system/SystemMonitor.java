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

package org.spreadme.commons.system;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;
import org.spreadme.commons.lang.SizeUnit;

/**
 * monitor service
 * @author shuwei.wang
 * @since 1.0.0
 */
public class SystemMonitor {

	private static Runtime runtime = Runtime.getRuntime();

	private static final String OS_NAME_KEY = "os.name";

	private static OsType osType;

	static {
		osType = OsType.resolve(System.getProperty(OS_NAME_KEY));
	}

	public SystemInfo getSystemInfo() {
		SystemInfo systemInfo = new SystemInfo();
		// jvm
		systemInfo.setTotalJvmMemory(runtime.totalMemory());
		systemInfo.setTotalJvmMemoryUnit(SizeUnit.convert(systemInfo.getTotalJvmMemory()));
		systemInfo.setFreeJvmMemory(runtime.freeMemory());
		systemInfo.setFreeJvmMemoryUnit(SizeUnit.convert(systemInfo.getFreeJvmMemory()));
		systemInfo.setMaxJvmMemory(runtime.maxMemory());
		systemInfo.setMaxJvmMemoryUnit(SizeUnit.convert(systemInfo.getMaxJvmMemory()));
		// os
		OperatingSystemMXBean osmxbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		systemInfo.setTotalMemory(osmxbean.getTotalPhysicalMemorySize());
		systemInfo.setTotalMemoryUnit(SizeUnit.convert(systemInfo.getTotalMemory()));

		systemInfo.setFreeMemory(osmxbean.getFreePhysicalMemorySize());
		systemInfo.setFreeMemoryUnit(SizeUnit.convert(systemInfo.getFreeMemory()));

		systemInfo.setUsedMemory(systemInfo.getTotalMemory() - systemInfo.getFreeMemory());
		systemInfo.setUsedMemoryUnit(SizeUnit.convert(systemInfo.getUsedMemory()));

		systemInfo.setProcessors(osmxbean.getAvailableProcessors());
		String osName = System.getProperty(OS_NAME_KEY);
		systemInfo.setOsName(osName);
		systemInfo.setOsType(osType);
		systemInfo.setArchType(ArchType.resolve(osmxbean.getArch()));
		return systemInfo;
	}
}
