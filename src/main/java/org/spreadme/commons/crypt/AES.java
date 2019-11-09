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

import java.util.Arrays;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.spreadme.commons.id.IdentifierGenerator;
import org.spreadme.commons.id.support.UUIDGenerator;

/**
 * aes encrypt
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class AES {

	private AES() {

	}

	public static String generateKey() {
		IdentifierGenerator<UUID> uuidGenerator = new UUIDGenerator();
		String uuid = uuidGenerator.nextIdentifier().toString();
		return uuid.substring(0, uuid.indexOf("-"));
	}

	public static byte[] encrypt(byte[] rawData, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(Algorithm.AES.getValue());
		key = extendKey(key);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, Algorithm.AES.getValue()));
		return cipher.doFinal(rawData);
	}

	public static byte[] decrypt(byte[] cipherData, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(Algorithm.AES.getValue());
		key = extendKey(key);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, Algorithm.AES.getValue()));
		return cipher.doFinal(cipherData);
	}

	private static byte[] extendKey(byte[] key) {
		key = Hash.toHexString(key, null, 0, Algorithm.MD5).getBytes();
		key = Arrays.copyOf(key, key.length / 2);
		return key;
	}
}
