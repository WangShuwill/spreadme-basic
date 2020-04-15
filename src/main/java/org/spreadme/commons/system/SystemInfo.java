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
import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

/**
 * system info
 * @author shuwei.wang
 * @since 1.0.0
 */
public final class SystemInfo {

	public static final int EOF = -1;

	private static final OperatingSystemMXBean MXBEAN;

	static {
		MXBEAN = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	}

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

	// Class Path路径
	public static final String CLASS_PATH = System.getProperty("java.class.path");

	// operating system name
	public static final String OS_NAME = System.getProperty("os.name");

	// operating system type
	public static final OsType OS_TYPE = OsType.resolve(OS_NAME);

	// arch type
	public static final ArchType ARCH_TYPE = ArchType.resolve(MXBEAN.getArch());

}
