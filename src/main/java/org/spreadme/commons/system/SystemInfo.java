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

import java.io.File;
import java.io.Serializable;

/**
 * system info
 * @author shuwei.wang
 * @since 1.0.0
 */
public class SystemInfo implements Serializable {

	private static final long serialVersionUID = 3798488087128864675L;

	public static final int EOF = -1;

	// 文件路径分隔符
	public static final String FILE_SEPARATOR = File.separator;

	// 换行符
	public static final String LINE_SEPARATOR = System.lineSeparator();

	// 临时目录
	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");

	// 应用的工作目录
	public static final String USER_DIR = System.getProperty("user.dir");

	// 用户 HOME目录
	public static final String USER_HOME = System.getProperty("user.home");

	// Java HOME目录
	public static final String JAVA_HOME = System.getProperty("java.home");

	private String osName;

	private OSType osType;

	private String arch;

	private int processors;

	private long totalJvmMemory;

	private String totalJvmMemoryUnit;

	private long freeJvmMemory;

	private String freeJvmMemoryUnit;

	private long maxJvmMemory;

	private String maxJvmMemoryUnit;

	private long totalMemory;

	private String totalMemoryUnit;

	private long freeMemory;

	private String freeMemoryUnit;

	private long usedMemory;

	private String usedMemoryUnit;

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public OSType getOsType() {
		return osType;
	}

	public void setOsType(OSType osType) {
		this.osType = osType;
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	public int getProcessors() {
		return processors;
	}

	public void setProcessors(int processors) {
		this.processors = processors;
	}

	public long getTotalJvmMemory() {
		return totalJvmMemory;
	}

	public void setTotalJvmMemory(long totalJvmMemory) {
		this.totalJvmMemory = totalJvmMemory;
	}

	public long getFreeJvmMemory() {
		return freeJvmMemory;
	}

	public void setFreeJvmMemory(long freeJvmMemory) {
		this.freeJvmMemory = freeJvmMemory;
	}

	public long getMaxJvmMemory() {
		return maxJvmMemory;
	}

	public void setMaxJvmMemory(long maxJvmMemory) {
		this.maxJvmMemory = maxJvmMemory;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public String getTotalJvmMemoryUnit() {
		return totalJvmMemoryUnit;
	}

	public void setTotalJvmMemoryUnit(String totalJvmMemoryUnit) {
		this.totalJvmMemoryUnit = totalJvmMemoryUnit;
	}

	public String getFreeJvmMemoryUnit() {
		return freeJvmMemoryUnit;
	}

	public void setFreeJvmMemoryUnit(String freeJvmMemoryUnit) {
		this.freeJvmMemoryUnit = freeJvmMemoryUnit;
	}

	public String getMaxJvmMemoryUnit() {
		return maxJvmMemoryUnit;
	}

	public void setMaxJvmMemoryUnit(String maxJvmMemoryUnit) {
		this.maxJvmMemoryUnit = maxJvmMemoryUnit;
	}

	public String getTotalMemoryUnit() {
		return totalMemoryUnit;
	}

	public void setTotalMemoryUnit(String totalMemoryUnit) {
		this.totalMemoryUnit = totalMemoryUnit;
	}

	public String getFreeMemoryUnit() {
		return freeMemoryUnit;
	}

	public void setFreeMemoryUnit(String freeMemoryUnit) {
		this.freeMemoryUnit = freeMemoryUnit;
	}

	public String getUsedMemoryUnit() {
		return usedMemoryUnit;
	}

	public void setUsedMemoryUnit(String usedMemoryUnit) {
		this.usedMemoryUnit = usedMemoryUnit;
	}

	@Override
	public String toString() {
		return "SystemInfo{" +
				"osName='" + osName + '\'' +
				", osType='" + osType + '\'' +
				", arch='" + arch + '\'' +
				", processors=" + processors +
				", totalJvmMemory=" + totalJvmMemory +
				", totalJvmMemoryUnit='" + totalJvmMemoryUnit + '\'' +
				", freeJvmMemory=" + freeJvmMemory +
				", freeJvmMemoryUnit='" + freeJvmMemoryUnit + '\'' +
				", maxJvmMemory=" + maxJvmMemory +
				", maxJvmMemoryUnit='" + maxJvmMemoryUnit + '\'' +
				", totalMemory=" + totalMemory +
				", totalMemoryUnit='" + totalMemoryUnit + '\'' +
				", freeMemory=" + freeMemory +
				", freeMemoryUnit='" + freeMemoryUnit + '\'' +
				", usedMemory=" + usedMemory +
				", usedMemoryUnit='" + usedMemoryUnit + '\'' +
				'}';
	}
}
