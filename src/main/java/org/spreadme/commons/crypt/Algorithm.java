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

package org.spreadme.commons.crypt;

/**
 * Algorithm
 * @author shuwei.wang
 * @since 1.0.0
 */
public enum Algorithm {

	DES("DES"),
	DES_CBC_PKCS5Padding("DES/CBC/PKCS5Padding"),
	SHA1("SHA-1"),
	SHA224("SHA-224"),
	SHA256("SHA-256"),
	SHA384("SHA-384"),
	SHA512("SHA-512"),
	MD5("MD5"),
	SHA("SHA"),
	AES("AES"),
	AES_CBC_PKCS5Padding("AES/CBC/PKCS5Padding"),
	RSA("RSA"),
	RSA_ECB_PKCS1Padding("RSA/ECB/PKCS1Padding"),
	SHA256withRSA("SHA256withRSA");

	private final String value;

	Algorithm(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
