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

/**
 * arch type
 * @author shuwei.wang
 * @since 1.0.0
 */
public enum ArchType {

	X32,
	X64,
	PPC,
	PPC64,
	AARCH_64,
	ARM,
	ARM64,
	UNKNOWN;

	public static ArchType resolve(String arch) {
		ArchType archType = ArchType.UNKNOWN;
		int bits;
		if (arch.contains("arm")) {
			archType = ArchType.ARM;
		}
		else if (arch.contains("aarch64")) {
			archType = ArchType.AARCH_64;
		}
		else if (arch.contains("ppc")) {
			bits = 32;
			if (arch.contains("64")) {
				bits = 64;
			}
			archType = (32 == bits) ? ArchType.PPC : ArchType.PPC64;
		}
		else if (arch.contains("86") || arch.contains("amd")) {
			bits = 32;
			if (arch.contains("64")) {
				bits = 64;
			}
			archType = (32 == bits) ? ArchType.X32 : ArchType.X64;
		}
		return archType;
	}
}
