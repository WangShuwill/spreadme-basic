/*
 * Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.system;

/**
 * Architecture
 * @author shuwei.wang
 */
public enum Architecture {

	UNKNOWN(OsType.UNKNOWN, ArchType.UNKNOWN),
	LINUX_32(OsType.LINUX, ArchType.X32),
	LINUX_64(OsType.LINUX, ArchType.X64),
	LINUX_ARM(OsType.LINUX, ArchType.ARM),
	LINUX_ARM64(OsType.LINUX, ArchType.ARM64),
	WINDOWS_32(OsType.WINDOWS, ArchType.X32),
	WINDOWS_64(OsType.WINDOWS, ArchType.X64),
	MACOS_32(OsType.MAXOS, ArchType.X32),
	MACOS_64(OsType.MAXOS, ArchType.X64),
	MACOS_PPC(OsType.MAXOS, ArchType.PPC),
	AIX_32(OsType.AIX, ArchType.X32),
	AIX_64(OsType.AIX, ArchType.X64);

	private OsType osType;

	private ArchType archType;

	Architecture(OsType osType, ArchType archType) {
		this.osType = osType;
		this.archType = archType;
	}

	public OsType getOsType() {
		return osType;
	}

	public void setOsType(OsType osType) {
		this.osType = osType;
	}

	public ArchType getArchType() {
		return archType;
	}

	public void setArchType(ArchType archType) {
		this.archType = archType;
	}
}
