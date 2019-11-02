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

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.spreadme.commons.codec.Hex;

/**
 * sha
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class Hash {

	public static byte[] get(InputStream in, Algorithm algorithm) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm.getName());
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) != -1) {
				digest.update(buffer, 0, length);
			}
			return digest.digest();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex.getMessage(), ex);
		}
	}

	public static String toHashString(InputStream in, Algorithm algorithm) {
		byte[] encoded = get(in, algorithm);
		return Hex.toHexString(encoded);
	}

	public static MessageDigest getMessageDigest(Algorithm algorithm) {
		try {
			return MessageDigest.getInstance(algorithm.getName());
		}
		catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException(ex.getMessage(), ex);
		}
	}
}
